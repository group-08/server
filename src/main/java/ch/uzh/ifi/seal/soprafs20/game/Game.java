package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.User.User;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.board.CasualBoard;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.field.Field;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    GameState gameState;

    @OneToMany(targetEntity = Player.class)
    List<Player> players = new ArrayList<>();

    @OneToOne
    Board board;

    @OneToOne
    Deck deck;

    @OneToOne
    User host;

    @OneToOne
    String name;

    @Enumerated
    WeatherState weatherState;

    public Game(User user, String name){
        this.board = new CasualBoard();
        this.name = name;
        this.deck = new Deck();
        this.gameState = GameState.PENDING;
        this.host = user;
        this.addPlayerFromUser(host);
    }

    /**
     * Checks whether a player can be added or the game is already full.
     * @return boolean if player can be added
     */
    private boolean canAddPlayer() {
        return this.players.size() <= 4; // Currently allow for games with 4 players
    }

    /**
     * Transforms the user into a player and adds him to the game's player list.
     * @param user User that should be added to game
     */
    public void addPlayerFromUser(User user) {
        if (this.canAddPlayer()) {
            this.players.add(this.transformUserIntoPlayer(user));
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
     * Prepares the game, fills game with bots, mixes players, assign colour and figures to players, shuffles deck and
     * sets the gameState to running
     */
    public void startGame(){
        // TODO: fill not full game with bots

        // Mix up the players
        Collections.shuffle(this.players);

        // TODO: Assign colour to players

        // Assign figures to players
        for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
            Player player = this.players.get(playerIndex);
            int firstField = 81+(playerIndex*4);
            int lastField = firstField+3;
            for (int figureIndex = firstField; figureIndex<=lastField; figureIndex++) {
                this.setPlayer(figureIndex, player);
            }
        }

        // Shuffle the cards
        this.deck.shuffle();

        // Set the gameState to running
        this.gameState = GameState.RUNNING;

    }

    /**
     * Initalizes the sixToTwo Round and loops it until there's a winner.
     */
    public void play(){
        /*
        while (game not finished) {
            this.sixToTwoRound();
        }
         */
    }

    /**
     * Let's to players change a card.
     */
    public void letPlayersChangeCard() {
        // Let partners change one card
    }

    /**
     * Distributes the correct number of cards to every player.
     * @param cardNum the number of cards to be distributed
     */
    public void distributeCards(int cardNum) {
        for (Player player : players) {
            List<Card> cards = this.deck.deal(cardNum);
            player.setHand(cards);
        }
    }

    /**
     * Sets number of cards to be distributed as 6, and loops a playRound counting down until cardNum is 2
     */
    public void sixToTwoRound() {
        int cardNum = 6;
        while (cardNum >= 2) {
            playRound(cardNum);
            cardNum--;
        }
    }

    /**
     * (TO DO)Distributes the correct amount of cards, let's player exchange cards, let's player make their moves until nobody
     * has cards left
     * @param cardNum the amount of cards to be distributed
     */
    public void playRound(int cardNum) {
        this.distributeCards(cardNum);

        this.letPlayersChangeCard();

        Player lastPlayer = players.get(players.size()-1);
        while (!lastPlayer.getHand().isEmpty()) {
            Player currentPlayer = this.getNextPlayer();
            playPlayersMove(currentPlayer);
        }
    }

    /**
     * (TO DO)Let's a player play one move.
     * @param currentPlayer player that has the turn
     */
    public void playPlayersMove(Player currentPlayer) {
        //Choose figure and card
        //Get all possible moves
        //Choose move
        //Perform move
        //Check if finished
        //Check if won
    }

    /**
     * Checks if all target fields of a player are occupied.
     * @param currentPlayer player whose targetfields you want to check
     * @return boolean if true
     */
    public Boolean checkIfFinished(Player currentPlayer){
        return board.checkIfAllTargetFieldsOccupied(id, currentPlayer);
    }

    /**
     * (TO DO)Checks if partner is finished.
     * @return boolean if true
     */
    public Boolean checkIfWon(){
        Player Partner = this.players.get(1);
        return board.checkIfAllTargetFieldsOccupied(id, Partner);
    }

    /**
     * (TO DO)Should stop all the loops and declare a winner.
     */
    public void endGame(){

    }

    /**
     * Gets the next player in the players list and adds it to the end of the list
     * @return the next player
     */
    public Player getNextPlayer(){
        // Get player on top of the array
        Player nextPlayer = this.players.get(0);

        // Append the player at the back again
        this.players.remove(nextPlayer);
        this.players.add(nextPlayer);

        // Return the player
        return nextPlayer;
    }

    /**
     * (TO DO)Given a Field id and a player, this function assigns the player to the figure on the field. Needed for setup.
     * @param id Field id
     * @param player Player to assign to figure
     */
    private void setPlayer(int id, Player player) {
        if (board.getField(id).getFigure() != null) {
            Field field = board.getField(id);
            Figure figure = field.getFigure();
            figure.setPlayer(player);
            player.addFigure(figure);
        } else {
            // throw error
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }



}
