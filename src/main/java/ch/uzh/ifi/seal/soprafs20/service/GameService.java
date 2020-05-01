package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
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

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService){
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    /**
     * Lets board compute all possible fields that can be reached from field with a specific card
     * @param move MovePostDTO that contains all information for the move (currentField and Card to play)
     * @return all possible targetFields that the player can move to from the specific field with the specific card
     */
    public ArrayList<Field> getPossibleFields(MovePostDTO move){

        Game actualGame = gameRepository.findById(move.getId()).orElse(null);
        assert actualGame != null;
        Board actualBoard = actualGame.getBoard();
        Card card = move.getCard();
        Figure figure = move.getFigure();
        Field currentField = figure.getField();

        return actualBoard.getPossibleFields(card, currentField);

    }

    /**
     * Lets the board actually move a figure from one field to another
     * @param move MovePostDTO that contains all the information for the move
     * @return the updated board after move is executed
     */
    public Board moveFigure(MovePostDTO move) {
        Game actualGame = gameRepository.findById(move.getId()).orElse(null);
        assert actualGame != null;
        Board actualBoard = actualGame.getBoard();
        Figure figure = move.getFigure();
        Field targetField = move.getTargetField();
        return actualBoard.move(figure, targetField);
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

    /**
     * (TO DO)Prepares the game, fills game with bots, mixes players, assign colour and figures to players, shuffles deck and
     * sets the gameState to running
     * @param gameId ID of game you want to start
     */
    public void setUpGame(long gameId){
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;

        // TODO: fill not full game with bots

        // Mix up the players
        Collections.shuffle(game.getPlayers());

        // TODO: Assign colour to players

        // Assign figures to players
        for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
            Player player = game.getPlayer(playerIndex);
            int firstField = 81+(playerIndex*4);
            int lastField = firstField+3;
            for (int figureIndex = firstField; figureIndex<=lastField; figureIndex++) {
                game.setPlayer(figureIndex, player);
            }
        }

        // Shuffle the cards
        game.getDeck().shuffle();

        // Set the gameState to running
        game.setGameState(GameState.RUNNING);

    }

    /**
     * Starts the game.
     * @param gameId game you want to start
     */
    public void startGame(long gameId) {
        this.playGame(gameId);
    }

    /**
     * (TO DO)Let's to players change a card.
     * @param gameId ID of game you want to let players exchange cards.
     */
    public void letPlayersChangeCard(long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);

        assert game != null;
        // Let partners change one card
    }

    /**
     * Distributes the correct number of cards to every player.
     * @param cardNum the number of cards to be distributed
     * @param gameId ID of game you want to distribute cards
     */
    public void distributeCards(long gameId, int cardNum) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        for (Player player : game.getPlayers()) {
            List<Card> cards = game.getDeck().deal(cardNum);
            player.setHand(cards);
        }
    }

    /**
     * (TO DO)Initalizes the sixToTwo Round and loops it until there's a winner.
     *      * @param gameId ID of game you want to play.
     */
    public void playGame(long gameId){
        // while (game not finished) {
            this.sixToTwoRound(gameId);
        //}
    }

    /**
     * Sets number of cards to be distributed as 6, and loops a playRound counting down until cardNum is 2
     * @param gameId ID of game you want to play the big round
     */
    public void sixToTwoRound(long gameId) {

        int cardNum = 6;
        while (cardNum >= 2) {
            this.playRound(gameId, cardNum);
            cardNum--;
        }
    }

    /**
     * (TO DO)Distributes the correct amount of cards, let's player exchange cards, let's player make their moves until nobody
     * has cards left
     * @param cardNum the amount of cards to be distributed
     * @param gameId ID of game you want to play a round
     */
    public void playRound(long gameId, int cardNum) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        this.distributeCards(gameId, cardNum);

        this.letPlayersChangeCard(gameId);

        Player lastPlayer = game.getPlayer(game.getPlayers().size()-1);
        while (!lastPlayer.getHand().isEmpty()) {
            Player currentPlayer = game.getNextPlayer();
            this.playPlayersMove(gameId, currentPlayer);
        }
    }

    /**
     * (TO DO)Let's a player play one move.
     * @param currentPlayer player that has the turn
     * @param gameId ID of game you want to play a move
     */
    public void playPlayersMove(long gameId, Player currentPlayer) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        //Choose figure and card
        //Get all possible moves
        //Choose move
        //Perform move
        //Check if current Player finished
        //If so, Check if won, and either end game or continue with partner
        //If not, continue playing
    }

    /**
     * Checks if all target fields of a player are occupied.
     * @param currentPlayer player whose targetfields you want to check
     * @param gameId ID of game you want to check if player has finished
     * @return boolean if true
     */
    public Boolean checkIfPlayerFinished(long gameId, Player currentPlayer){
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        Board board = game.getBoard();
        return board.checkIfAllTargetFieldsOccupied(currentPlayer);
    }

    /**
     * Checks if team of current player has won.
     * @param gameId ID of game you want to check if someone won
     * @return boolean if true
     */
    public Boolean checkIfWon(long gameId){
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        Player currentPlayer = game.getPlayer(3);
        Player partner = game.getPlayer(1);
        return checkIfPlayerFinished(gameId, currentPlayer) && checkIfPlayerFinished(gameId, partner);
    }

    /**
     * (TO DO)Should stop all the loops and declare a winner.
     * @param gameId ID of Game that should be ended
     */
    public void endGame(long gameId){
        // end all playing loops and declare winnder
    }


    public void updateGame(long id, Board board, Card card){

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

    public boolean checkToken(Long gameId, String tokenToCheck){
        Game actualGameToCheck = gameRepository.getOne(gameId);
        User Host = actualGameToCheck.getHost();
        User UserBelongingToToken = userService.getUserByToken(tokenToCheck);
        return Host==UserBelongingToToken;
    }

    public boolean checkIfUserExists(String tokenOfUser){
        return userService.getUserByToken(tokenOfUser) == null;
    }

    public List<Game> getAllLobbies(){
        return gameRepository.findAll();
    }

    public User getUserByToken(String token){
        return userService.getUserByToken(token);
    }

    public void createLobby(User userOwner, String gameName){
        Game game = new Game(userOwner, gameName);
        game = gameRepository.saveAndFlush(game);
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


}
