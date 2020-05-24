package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.field.FirstField;
import ch.uzh.ifi.seal.soprafs20.field.GoalField;
import ch.uzh.ifi.seal.soprafs20.field.HomeField;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.game.LogItem;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class GameService {

    private UserService userService;
    private DeckService deckService;
    private PlayerService playerService;
    private WeatherService weatherService;
    private final BoardService boardService;
    private final GameRepository gameRepository;
    private UserRepository userRepository;
    private FieldRepository fieldRepository;
    private FigureRepository figureRepository;
    private CardRepository cardRepository;
    private PlayerRepository playerRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("cardRepository") CardRepository cardRepository,
                       @Qualifier("deckRepository") DeckRepository deckRepository,
                       @Qualifier("playerRepository") PlayerRepository playerRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("fieldRepository") FieldRepository fieldRepository,
                       @Qualifier("userService") UserService userService,
                       @Qualifier("boardRepository") BoardRepository boardRepository,
                       @Qualifier("figureRepository") FigureRepository figureRepository
                       ) {

        this.userService = userService;
        this.cardRepository = cardRepository;
        this.figureRepository = figureRepository;
        this.gameRepository = gameRepository;
        this.fieldRepository = fieldRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.boardService = new BoardService(boardRepository, gameRepository);
        this.playerService = new PlayerService(playerRepository, boardRepository, gameRepository);
        this.userService = new UserService(userRepository);
        this.deckService = new DeckService(deckRepository, cardRepository);
        this.weatherService = new WeatherService();
    }

    /**
     * Gets the next player in the players list and adds it to the end of the list
     * @return the next player
     */
    public void rotatePlayersUntilNextPossible(Game game){

        List<Player> players = game.getPlayers();
        Player currentPlayer = players.get(0);
        players.remove(currentPlayer);
        players.add(currentPlayer);

        // Get player on top of the array
        Player nextPlayer = players.get(0);

        while ((!(playerService.checkIfCanPlay(game, nextPlayer.getId())) && this.checkIfCardsLeft(game))) {
            playerService.removeAllFromHand(nextPlayer);
            players.remove(nextPlayer);
            players.add(nextPlayer);
            nextPlayer = players.get(0);
        }

        this.gameRepository.saveAndFlush(game);
    }

    public void rotateIfNotPossible(Game game){

        List<Player> players = game.getPlayers();

        // Get player on top of the array
        Player nextPlayer = players.get(0);

        while ((!(playerService.checkIfCanPlay(game, nextPlayer.getId())) && this.checkIfCardsLeft(game))) {
            playerService.removeAllFromHand(nextPlayer);
            players.remove(nextPlayer);
            players.add(nextPlayer);
            nextPlayer = players.get(0);
        }

        this.gameRepository.saveAndFlush(game);
    }

    public Card getCardFromId(long cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        assert card != null;
        return card;
    }

    public Figure getFigureFromId(long figureId) {
        Figure figure = figureRepository.findById(figureId).orElse(null);
        assert figure != null;
        return figure;
    }

    public Field getFieldFromId(long fieldId) {
        Field targetField = fieldRepository.findById(fieldId).orElse(null);
        assert targetField != null;
        return targetField;
    }

    /**
     * Lets board compute all possible fields that can be reached from field with a specific card
     * @param move MovePostDTO that contains all information for the move (currentField and Card to play)
     * @return all possible targetFields that the player can move to from the specific field with the specific card
     */
    public ArrayList<Field> getPossibleFields(long gameId, MovePostDTO move){

        Game actualGame = gameRepository.findById(gameId).orElse(null);
        assert actualGame != null;
        long cardId = move.getCardId();
        long figureId = move.getFigureId();
        Card card = getCardFromId(cardId);
        Figure figure = getFigureFromId(figureId);
        Field currentField = figure.getField();

        if (card.getValue() == Value.JOKER) {
            return boardService.getPossibleFieldsJoker(actualGame, currentField);
        }
        if (card.getValue() == Value.SEVEN) {
            return boardService.getPossibleFieldsSeven(card, currentField);
        }
        else if (card.getValue() == Value.JACK) {
            return boardService.getPossibleFieldsJack(actualGame, currentField);
        } else {
            return boardService.getPossibleFields(actualGame, card, currentField);
        }
    }

    public boolean swapCheck(Field targetField, Field currentField, Player partner){
        if(currentField instanceof HomeField){
            return false;
        }
        if(targetField.getOccupant()!=null && targetField.getOccupant().getPlayer().getId()==partner.getId()){
            return true;
        }
        int level = 0;
        Queue<Field> queue = new LinkedList<>();
        queue.add(currentField);
        queue.add(null);
        Field temp = currentField;
        assert temp != null;
        while(!queue.isEmpty()){
            temp = queue.poll();
            if(temp == null){
                level++;
                queue.add(null);
            }
            else{
                if(temp.getId()==targetField.getId()){
                    return level>=14&&level!=60;
                }
                List<Field> adjFields = new ArrayList<>(temp.getAdjacencyList());
                queue.addAll(adjFields);
            }
        }
        return level>=14&&level!=60;
    }

        public Board playPlayersMove(long gameId, MovePostDTO move) {
        // get the game from gameId
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // set currentPlayer, partner, and rotate players
        Player currentPlayer = game.getPlayer(0);
        Player partner = playerService.getPartner(game, currentPlayer);

        //remove card from player
        long cardId = move.getCardId();
        long fieldId = move.getTargetFieldId();
        long figureId = move.getFigureId();
        Card card = getCardFromId(cardId);
        Field targetField = getFieldFromId(fieldId);
        Figure figure = getFigureFromId(figureId);

        updateLogItem(card,currentPlayer,game);
        playerService.removeFromHand(currentPlayer, card);

        if (card.getValue() == Value.JACK || (card.getValue()==Value.JOKER && swapCheck(targetField, figure.getField(), partner))) {
            this.swapFigure(game, figure, targetField);
        } else {
            this.moveFigure(game, figure, targetField);
        }

        // check if player is finished and if partner is finished

        if (playerService.checkIfPlayerFinished(game, currentPlayer)) {
            currentPlayer.setFinished(true);
            if (playerService.checkIfPlayerFinished(game, partner)) {
                game.setGameState(GameState.FINISHED);
                increaseScore(currentPlayer,partner);
                game.setGameState(GameState.FINISHED);
            }
        }


        this.rotatePlayersUntilNextPossible(game);


        if(!checkIfCardsLeft(game)) {

                    distributeCards(game, game.getCardNum());
                    game.decreaseCardNum();
                    this.setExchangeCard(game,true);
            weatherService.updateWeather(game);
            boardService.checkFieldsWeatherChange(game);
        }
        game.getBoard().setPassedTime(System.currentTimeMillis()/1000);

        gameRepository.saveAndFlush(game);

        return game.getBoard();
    }

    public void setExchangeCard(Game game, boolean exchangeCard) {
        for (Player player : game.getPlayers()) {
            player.setExchangeCards(exchangeCard);
        }
    }

    public int playPlayersMoveSeven(long gameId, MovePostDTO move) {
        // get the game from gameId
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // set currentPlayer, partner, and rotate players
        Player currentPlayer = game.getPlayer(0);
        Player partner = game.getPlayer(2);

        long cardId = move.getCardId();
        long figureId = move.getFigureId();
        long fieldId = move.getTargetFieldId();
        Card card = getCardFromId(cardId);
        Figure figure = getFigureFromId(figureId);
        Field targetField = getFieldFromId(fieldId);

        int remainingSteps = card.getRemainingSteps();


        int newRemaining = this.moveSeven(game, figure, targetField, remainingSteps);

        gameRepository.saveAndFlush(game);

        card.setRemainingSteps(newRemaining);
        cardRepository.saveAndFlush(card);

        newRemaining = checkIfFurtherMovesPossible(newRemaining, game, figure, move, cardId);

        card.setRemainingSteps(newRemaining);
        cardRepository.saveAndFlush(card);

        game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // check if player is finished and if partner is finished
        if (playerService.checkIfPlayerFinished(game, currentPlayer)) {
            currentPlayer.setFinished(true);
            if (playerService.checkIfPlayerFinished(game, partner)) {
                partner.setFinished(true);
                increaseScore(currentPlayer,partner);
                game.setGameState(GameState.FINISHED);
            }
        }


        if (newRemaining == 0) {
            //remove card from player


            updateLogItem(card,currentPlayer,game);
            playerService.removeFromHand(currentPlayer, card);

            this.rotatePlayersUntilNextPossible(game);
            // check if game still running and no cards left, distribute new cards

            if(game.getGameState() == GameState.RUNNING && !checkIfCardsLeft(game)) {
                    distributeCards(game, game.getCardNum());
                    game.decreaseCardNum();
                    this.setExchangeCard(game, true);

                weatherService.updateWeather(game);
                boardService.checkFieldsWeatherChange(game);
            }

            game.getBoard().setPassedTime(System.currentTimeMillis()/1000);
        }

        gameRepository.saveAndFlush(game);

        return newRemaining;


    }

    public void updateLogItem(Card card, Player player, Game game){
        List<LogItem> logItems = game.getLogItems();
        LogItem logItem = new LogItem(card.getSuit() ,card.getValue(), player.getId());
        logItems.add(logItem);
        if(logItems.size()>10){
            logItems.remove(0);
        }
        gameRepository.saveAndFlush(game);
    }

    public int checkIfFurtherMovesPossible(int newRemaining, Game game, Figure figure, MovePostDTO move, Long cardId){
        long gameID = game.getId();
        game = gameRepository.findById(gameID).orElse(null);
        assert game != null;
        Player player = figure.getPlayer();
        boolean movePossible = false;
        if (newRemaining!=0) {
            for (Figure figureOfPlayer : playerService.getOwnOrPartnerFigures(game, player)) {
                MovePostDTO newMove = new MovePostDTO();
                newMove.setCardId(cardId);
                newMove.setFigureId(figureOfPlayer.getId());
                List<Field> fields = this.getPossibleFields(gameID, newMove);
                if (!fields.isEmpty()) {
                    movePossible=true;
                }
            }
        }
        if(movePossible){
            return newRemaining;
        }
        else{
            return 0;
        }
    }


    public void increaseScore(Player currentPlayer, Player partner){
        if (currentPlayer.getUser()!=null){
            UserGetDTO currentUserDTO = currentPlayer.getUser();
            long currentPlayerId = currentUserDTO.getId();
            User currentPlayerUser = userService.getUserById(currentPlayerId);
            int currentPlayerUserScore = currentPlayerUser.getLeaderBoardScore();
            currentPlayerUser.setLeaderBoardScore((currentPlayerUserScore+1));
            userRepository.saveAndFlush(currentPlayerUser);
            }
        if(partner.getUser()!=null) {
            UserGetDTO partnerUserDTO = partner.getUser();
            long partnerId = partnerUserDTO.getId();
            User partnerUser = userService.getUserById(partnerId);
            int partnerUserScore = partnerUser.getLeaderBoardScore();
            partnerUser.setLeaderBoardScore((partnerUserScore + 1));
            userRepository.saveAndFlush(partnerUser);}
    }

    public int moveSeven(Game game, Figure figure, Field targetField, int remaining)  {
        return boardService.moveSeven(game, figure, targetField, remaining);
    }

    public void swapFigure(Game game, Figure figure, Field targetField) {
        boardService.swapJack(game, figure, targetField);
    }

    /**
     * Lets the board actually move a figure from one field to another
     * @return the updated board after move is executed
     */
    public void moveFigure(Game actualGame, Figure figure, Field field) {
        boardService.move(actualGame, figure, field);
    }

    /**
     * Checks whether a player can be added or the game is already full.
     * @param gameId ID of the game you want to modify
     * @return boolean if player can be added
     */
    private boolean canAddPlayer(long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        return game.getPlayers().size() <= 4; // Currently allow for games with 4 players
    }

    /**
     * Transforms the user into a player and adds him to the game's player list.
     * @param gameId ID of the game you want to modify
     * @param user User that should be added to game
     */
    public void addPlayerFromUser(long gameId, User user) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        if (this.canAddPlayer(gameId)) {
            game.getPlayers().add(this.transformUserIntoPlayer(user));
        }
        else {
            throw new ArrayIndexOutOfBoundsException("Game is already full.");
        }
        this.gameRepository.save(game);
    }

    /**
     * Takes a user and transforms it into a player without a hand, colour or figure.
     * @param user user to be transformed
     * @return the player that has been created
     */
    public Player transformUserIntoPlayer(User user) {
        Player player = new Player();
        player.setUser(user);
        return player;
    }

    public void setColourOfPlayer(Player player, Colour colour) {
        player.setColour(colour);
    }

    public Player createRoboPlayer(){
        return new Player();
    }


    private void fillGame(Game game){
        List<Player> humans = game.getPlayers();
        while (humans.size() < 4){
            Player roboPlayer = createRoboPlayer();
            humans.add(roboPlayer);
        }
        game.setPlayers(humans);
        gameRepository.saveAndFlush(game);
    }

    /**
     * (TO DO)Prepares the game, fills game with bots, mixes players, assign colour and figures to players, shuffles deck and
     * sets the gameState to running
     * @param gameId ID of game you want to start
     */
    public void setUpGame(long gameId){
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        this.fillGame(game);

        game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // Mix up the players
        Collections.shuffle(game.getPlayers());

        this.setColourOfPlayer(game.getPlayer(0), Colour.BLUE);
        this.setColourOfPlayer(game.getPlayer(1), Colour.GREEN);
        this.setColourOfPlayer(game.getPlayer(2), Colour.YELLOW);
        this.setColourOfPlayer(game.getPlayer(3), Colour.RED);

        // set boolean finished of all players on false
        for (Player player : game.getPlayers()) {
            player.setFinished(false);
        }

        // Assign figures to players
        for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
            Player player = game.getPlayer(playerIndex);
            int fieldIndex = playerIndex*16+1;
            //assign player to firstFields
            FirstField firstFieldToAssignPlayer = (FirstField)game.getBoard().getField(fieldIndex);
            firstFieldToAssignPlayer.setPlayer(player);
            //assign all figures to player, all players to their figures and all players to homeFields
            int firstField = 81+(playerIndex*4);
            int lastField = firstField+3;
            for (int figureIndex = firstField; figureIndex<=lastField; figureIndex++) {
                game.assignPlayerandFigureandHomeField(figureIndex, player);
            }
        }

        // Assign GoalFields
        for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
            Player player = game.getPlayer(playerIndex);
            int firstGoalfield = 65+(playerIndex*4);
            int lastGoalfield = firstGoalfield+3;
            for (int i = firstGoalfield; i <= lastGoalfield; i++) {
                ((GoalField) game.getBoard().getField(i)).setPlayer(player);
            }
        }

        /// fill the deck with cards and shuffle those
        deckService.createDeck(game.getDeck());

        if (!this.checkIfCardsLeft(game))    {
            this.distributeCards(game, game.getCardNum());
            game.decreaseCardNum();
            this.setExchangeCard(game, true);
        }

        // Set the gameState to running
        game.setGameState(GameState.RUNNING);


        this.gameRepository.saveAndFlush(game);

    }

    public void playRoboMove(long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        long id = game.getId();

        Player player = game.getPlayers().get(0);

        MovePostDTO move = automaticMove(player, id);

        long cardId = move.getCardId();
        Card card = getCardFromId(cardId);
        long playerId = player.getId();
        if (card.getValue() == Value.SEVEN) {
            while (card.getRemainingSteps() > 0) {
                player = playerService.findById(playerId);
                assert player != null;
                move = automaticMoveSeven(id, card, player);
                playPlayersMoveSeven(game.getId(), move);
                card = cardRepository.findById(cardId).orElse(null);
                assert card != null;
            }
        }
        else {
            playPlayersMove(game.getId(), move);
            }
    }

    public Player getPlayerFromUser(User user) {
        return playerRepository.findByUser(user);
    }

    /**
     * (TO DO)Let's to players change a card.
     * @param gameId ID of game you want to let players exchange cards.
     */
    public Game letPlayersChangeCard(long gameId, long playerId, long cardId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        Card card = cardRepository.findById(cardId).orElse(null);
        assert card != null;
        Player player = playerRepository.findById(playerId).orElse(null);
        assert player != null;
        playerService.exchange(gameId, playerId, card);
        player.setExchangeCards(false);
        // Check if all players exchanged cards, if so, rotate if player cannot play
        boolean allDone = true;
        for (Player actualPlayer : game.getPlayers()) {
            if (actualPlayer.getExchangeCards() && actualPlayer.getUser() != null) {
                allDone = false;
                break;
            }
        }
        if (allDone) {
            List<Player> players = game.getPlayers();

            for(Player botPlayer : players){
                if(botPlayer.getUser()==null && botPlayer.getExchangeCards()){
                    playerService.exchange(gameId, botPlayer.getId(), botPlayer.getHand().get(0));
                    botPlayer.setExchangeCards(false);
                    playerRepository.saveAndFlush(botPlayer);
                }
            }
            boolean someoneCanPlay = false;
            this.rotateIfNotPossible(game);
            for(Player checkPlayerHand : game.getPlayers()){
                if(!checkPlayerHand.getHand().isEmpty()){
                    someoneCanPlay = true;
                    break;
                }
            }
            if(!someoneCanPlay){
                game.decreaseCardNum();
                distributeCards(game, game.getCardNum());
                this.setExchangeCard(game, true);
            }
            game.getBoard().setPassedTime(System.currentTimeMillis()/1000);

        }
        this.gameRepository.saveAndFlush(game);
        return game;
    }

    /**
     * Distributes the correct number of cards to every player.
     * @param cardNum the number of cards to be distributed
     * @param game ID of game you want to distribute cards
     */
    public void distributeCards(Game game, int cardNum) {
        weatherService.updateWeather(game);
        boardService.checkFieldsWeatherChange(game);
        ///this function checks if the deck contains enough cards. if not it refills the deck
        deckService.checkIfEnoughCardsLeft( cardNum, game.getDeck().getId());
        for (Player player : game.getPlayers()) {

            ///changed this to use the functionality of the deck service
            List<Card> cards = deckService.drawCards(cardNum, game.getDeck().getId());
            player.setHand(cards);
        }
    }

    public boolean checkIfCardsLeft(Game game)   {
        for (Player player : game.getPlayers()) {
            if (!player.getHand().isEmpty()) {
                return true;
            }
        }
        return false;
    }


    public Game getLobbyById(long id){
        return gameRepository.findById(id).orElse(null);
    }

    public void addUser(Long gameId, String tokenOfUser){
        User userToBeAdded = userService.getUserByToken(tokenOfUser);
        Game actualGame = gameRepository.findById(gameId).orElse(null);
        if(actualGame!=null) {
            this.addPlayerFromUser(gameId, userToBeAdded);
        }
    }

    public void removeUser(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game!=null;
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        Player player = playerRepository.findByUser(user);
        game.getPlayers().remove(player);

        playerRepository.delete(player);
        playerRepository.flush();
        gameRepository.saveAndFlush(game);

    }

    public void removeSelf(Long gameId, String token, Long userId) {
        User user = userRepository.findByToken(token);
        if (user.getId().equals(userId)) {
            this.removeUser(gameId, userId);
        }
    }

    public boolean checkToken(Long gameId, String tokenToCheck){
        Game actualGameToCheck = gameRepository.findById(gameId).orElse(null);
        assert actualGameToCheck != null;
        User host = actualGameToCheck.getHost();
        User userBelongingToToken = userService.getUserByToken(tokenToCheck);
        return host == userBelongingToToken;
    }

    public boolean checkIfUserExists(String tokenOfUser){
        return userService.getUserByToken(tokenOfUser) != null;
    }

    public List<LobbyGetDTO> getAllLobbies(){


        List<Game> allGames = gameRepository.findAll();
        List<LobbyGetDTO> games = new ArrayList<>();
        for(Game game : allGames){
            games.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(game));
        }
        return games;
    }

    public User getUserByToken(String token){
        return userService.getUserByToken(token);
    }

    public LobbyGetDTO createLobby(User userOwner, String gameName){
        Game game = new Game(userOwner, gameName);
        gameRepository.saveAndFlush(game);
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(game);
    }

    public boolean timedelta(Game game){
        long time = game.getBoard().getPassedTime();
        long currentTime = System.currentTimeMillis()/1000;
        return currentTime-time > 1;
    }

    public boolean hostCheck(Game game, String token){
         User host = game.getHost();
         User tokenOwner = getUserByToken(token);
         return host.getId().equals(tokenOwner.getId());
    }

    public boolean roboCheck(Game game){
        return game.getPlayer(0).getUser()==null;
    }

    public GameGetDTO getGameById(Long id, String token){
        Game game =  gameRepository.findById(id).orElse(null);
        assert game != null;
        long gameId = game.getId();

        if (allPlayersExchanged(game) && roboCheck(game) && hostCheck(game, token) && timedelta(game)) {
            playRoboMove(gameId);
        }

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    public boolean allPlayersExchanged(Game game) {
        boolean exchanged = true;
        for (Player player : game.getPlayers()) {
            if (player.getExchangeCards()) {
                exchanged = false;
                break;
            }
        }
        return exchanged;
    }


    public MovePostDTO automaticMove(Player player, long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        for (Figure figure : playerService.getOwnOrPartnerFigures(game, player))  {
            for (Card card : player.getHand()) {
                MovePostDTO move = new MovePostDTO();
                move.setCardId(card.getId());
                move.setFigureId(figure.getId());
                List<Field> fields = this.getPossibleFields(gameId, move);
                if (!fields.isEmpty()) {
                    Collections.shuffle(fields);
                    move.setTargetFieldId(fields.get(0).getId());
                    return move;
                }
            }
        }
        return null;
    }

    public MovePostDTO automaticMoveSeven(long gameId, Card card, Player player) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        for (Figure figure : playerService.getOwnOrPartnerFigures(game, player)) {
            MovePostDTO move = new MovePostDTO();
            move.setCardId(card.getId());
            move.setFigureId(figure.getId());
            List<Field> fields = this.getPossibleFields(gameId, move);
            if (!fields.isEmpty()) {
                Collections.shuffle(fields);
                move.setTargetFieldId(fields.get(0).getId());
                return move;
            }
        }
        return new MovePostDTO();
    }

    public GameFinishedDTO findWinners(long gameID){

        Game game = gameRepository.findById(gameID).orElse(null);
        assert game != null;

        List<Player> players = game.getPlayers();

        GameFinishedDTO gameFinishedDTO = new GameFinishedDTO();

        for (Player itPlayer : players){

            if (playerService.checkIfPlayerFinished(game, itPlayer)){

                int indexOfPlayer = players.indexOf(itPlayer);
                Player partner = players.get((indexOfPlayer + 2) % 4);

                if(playerService.checkIfPlayerFinished(game,partner)){
                    List<UserGetDTO> userList = new ArrayList<>();
                    if (itPlayer.getUser() != null){
                        UserGetDTO playerUser = itPlayer.getUser();
                        userList.add(playerUser);
                    }

                    if (partner.getUser() != null){
                        UserGetDTO partnerUser = partner.getUser();
                        userList.add(partnerUser);
                    }

                    gameFinishedDTO.setWinners(userList);
                    return gameFinishedDTO;
                }
            }
        }
        return null;
    }

    public void deleteGame(long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game!=null;
        gameRepository.delete(game);
    }



}
