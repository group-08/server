package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class GameService {

    private UserService userService;
    private DeckService deckService;
    private PlayerService playerService;
    private final BoardService boardService;
    private final GameRepository gameRepository;
    private CardService cardService;
    private FieldRepository fieldRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("cardRepository") CardRepository cardRepository,
                       @Qualifier("deckRepository") DeckRepository deckRepository,
                       @Qualifier("playerRepository") PlayerRepository playerRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("fieldRepository") FieldRepository fieldRepository)
    {
        this.gameRepository = gameRepository;
        this.fieldRepository = fieldRepository;
        this.boardService = new BoardService(gameRepository);
        this.playerService = new PlayerService(playerRepository, gameRepository);
        this.userService = new UserService(userRepository);
        this.cardService = new CardService(cardRepository);
        this.deckService = new DeckService(deckRepository, cardRepository);
    }

    /**
     * Gets the next player in the players list and adds it to the end of the list
     * @return the next player
     */
    public Player getNextPlayer(Game game){
        List<Player> players = game.getPlayers();

        // Get player on top of the array
        Player nextPlayer = players.get(0);

        while (!playerService.checkIfCanPlay(game, nextPlayer.getId()) && this.checkIfCardsLeft(game)) {
            playerService.removeAllFromHand(nextPlayer);
            players.remove(nextPlayer);
            players.add(nextPlayer);
            nextPlayer = players.get(0);
        }

        players.remove(nextPlayer);
        players.add(nextPlayer);

        this.gameRepository.saveAndFlush(game);

        // Return the player
        return nextPlayer;

    }

    /**
     * Lets board compute all possible fields that can be reached from field with a specific card
     * @param move MovePostDTO that contains all information for the move (currentField and Card to play)
     * @return all possible targetFields that the player can move to from the specific field with the specific card
     */
    public ArrayList<Field> getPossibleFields(MovePostDTO move){

        Game actualGame = gameRepository.findById(move.getId()).orElse(null);
        assert actualGame != null;
        Card card = move.getCard();
        Figure figure = move.getFigure();
        Field currentField = figure.getField();

        if (card.getValue() == Value.SEVEN) {
            return boardService.getPossibleFieldsSeven(card, currentField, 7);
        } else if (card.getValue() == Value.JACK) {
            return boardService.getPossibleFieldsJack(actualGame, card, currentField);
        } else {
            return boardService.getPossibleFields(actualGame, card, currentField);
        }
    }

    public Board playPlayersMove(MovePostDTO move) {
        // get the game from gameId
        long gameId = move.getId();
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // set currentPlayer, partner, and rotate players
        Player currentPlayer = this.getNextPlayer(game);
        Player partner = game.getPlayer(1);

        //remove card from player
        playerService.removeFromHand(currentPlayer, move.getCard());

        // move figure
        Figure figure = move.getFigure();
        Field field = move.getTargetField();
        this.moveFigure(game, figure, field);

        // check if player is finished and if partner is finished
        if (checkIfPlayerFinished(game, currentPlayer)) {
            if (checkIfPlayerFinished(game, partner)) {
                game.setGameState(GameState.FINISHED);
            } else {
                // take over partners figures
            }
        }

        // check if game still running and no cards left, distribute new cards
        if (game.getGameState() == GameState.RUNNING && !checkIfCardsLeft(game)) {
            distributeCards(game, game.getCardNum());
            game.decreaseCardNum();
            game.setExchangeCard(true);
        }

        gameRepository.saveAndFlush(game);

        return game.getBoard();
    }

    /**
     * Lets the board actually move a figure from one field to another
     * @return the updated board after move is executed
     */
    public Board moveFigure(Game actualGame, Figure figure, Field field) {
        return boardService.move(actualGame, figure, field);
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

    /**
     * (TO DO)Prepares the game, fills game with bots, mixes players, assign colour and figures to players, shuffles deck and
     * sets the gameState to running
     * @param gameId ID of game you want to start
     */
    public void setUpGame(long gameId){
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // Mix up the players
        Collections.shuffle(game.getPlayers());

        this.setColourOfPlayer(game.getPlayer(0), Colour.BLUE);
        this.setColourOfPlayer(game.getPlayer(1), Colour.GREEN);
        this.setColourOfPlayer(game.getPlayer(2), Colour.YELLOW);
        this.setColourOfPlayer(game.getPlayer(3), Colour.RED);


        // Assign figures to players
        for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
            Player player = game.getPlayer(playerIndex);
            int firstField = 81+(playerIndex*4);
            int lastField = firstField+3;
            for (int figureIndex = firstField; figureIndex<=lastField; figureIndex++) {
                game.assignPlayerandFigure(figureIndex, player);
            }
        }

        /// fill the deck with cards and shuffle those
        deckService.createDeck(game.getDeck());


        this.distributeCards(game, game.getCardNum());
        game.decreaseCardNum();
        game.setExchangeCard(true);

        // Set the gameState to running
        game.setGameState(GameState.RUNNING);


        this.gameRepository.saveAndFlush(game);

    }

    /**
     * (TO DO)Let's to players change a card.
     * @param gameId ID of game you want to let players exchange cards.
     */
    public Game letPlayersChangeCard(long gameId, long userId, Card card) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        playerService.exchange(gameId, userId, card);
        game.setExchangeCard(false);
        this.gameRepository.saveAndFlush(game);
        return game;
    }

    /**
     * Distributes the correct number of cards to every player.
     * @param cardNum the number of cards to be distributed
     * @param game ID of game you want to distribute cards
     */
    public void distributeCards(Game game, int cardNum) {
        ///this function checks if the deck contains enough cards. if not it refills the deck
        deckService.checkIfEnoughCardsLeft( cardNum, game.getDeck().getId());
        for (Player player : game.getPlayers()) {

            ///changed this to use the functionality of the deck service
            List<Card> cards = deckService.drawCards(cardNum, game.getDeck().getId());
            player.setHand(cards);
        }
    }

    public boolean checkIfCardsLeft(Game game)   {
        boolean check = false;
        for (Player player : game.getPlayers()) {
            if (player.getHand() != null) {
                check = true;
                break;
            }
        }
        return check;
    }

    /**
     * Checks if all target fields of a player are occupied.
     * @param currentPlayer player whose targetfields you want to check
     * @param game ID of game you want to check if player has finished
     * @return boolean if true
     */
    public Boolean checkIfPlayerFinished(Game game, Player currentPlayer){
        return boardService.checkIfAllTargetFieldsOccupied(game, currentPlayer);
    }

    public GameGetDTO getLobbyById(long id){
        Game game = gameRepository.findById(id).orElse(null);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    public void addUser(Long gameId, String tokenOfUser){
        User userToBeAdded = userService.getUserByToken(tokenOfUser);
        Game actualGame = gameRepository.findById(gameId).orElse(null);
        if(actualGame!=null) {
            this.addPlayerFromUser(gameId, userToBeAdded);
        }
    }

    public boolean checkToken(Long gameId, String tokenToCheck){
        Game actualGameToCheck = gameRepository.findById(gameId).get();
        User Host = actualGameToCheck.getHost();
        User UserBelongingToToken = userService.getUserByToken(tokenToCheck);
        return Host == UserBelongingToToken;
    }

    public boolean checkIfUserExists(String tokenOfUser){
        return userService.getUserByToken(tokenOfUser) != null;
    }

    public List<GameGetDTO> getAllLobbies(){


        List<Game> allGames = gameRepository.findAll();
        List<GameGetDTO> games = new ArrayList<>();
        for(Game game : allGames){
            games.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return games;
    }

    public User getUserByToken(String token){
        return userService.getUserByToken(token);
    }

    public GameGetDTO createLobby(User userOwner, String gameName){
        Game game = new Game(userOwner, gameName);
        gameRepository.saveAndFlush(game);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    public Game getGameById(Long id){
        return gameRepository.findById(id).orElse(null);
    }
    /*
    public void deleteUser(Long id, UserPostDTO userToBeDeletedDTO){
        String userToBeAddedUsername = userToBeDeletedDTO.getUsername();
        User userToBeDeleted = userRepository.findByUsername(userToBeAddedUsername);
        Game actualGame = gameRepository.findById(id).orElse(null);
        if(actualGame!=null) {
            actualGame.removePlayer(userToBeDeleted);
        }
        //We need function to delete User; Takes id and User;
    }
     */

    // For testing reasons
    public MovePostDTO automaticMove(Player player, long gameId) {
        Card cardKing = new NormalCard(Suit.SPADES, Value.ACE);
        player.getHand().remove(0);
        player.getHand().add(cardKing);
        for (Figure figure : player.getFigures())  {
            for (Card card : player.getHand()) {
                if (card instanceof JokerCard) {
                    ((JokerCard) card).setValue(Value.KING);
                } else if (card.getValue() == Value.SEVEN) {
                    ((NormalCard) card).setValue(Value.ACE);
                }
                MovePostDTO move = new MovePostDTO();
                move.setId(gameId);
                move.setCard(card);
                move.setFigure(figure);
                List<Field> fields = this.getPossibleFields(move);
                if (!fields.isEmpty()) {
                    move.setTargetField(fields.get(0));
                    return move;
                }
            }
        }
        return null;
    }


}
