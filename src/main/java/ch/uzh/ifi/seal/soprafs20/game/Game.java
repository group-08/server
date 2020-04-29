package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @OneToOne
    Board board;

    @OneToOne
    Deck deck;

    @Enumerated
    WeatherState weatherState;

    Game(){
        this.board = new CasualBoard();
        this.deck = new Deck();
        this.gameState = GameState.PENDING;

        // Make first user the master player and transform users to players.
    }

    public void startGame(){

        // Mix up the players
        Collections.shuffle(this.players);

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

    private boolean canAddPlayer() {
        return this.players.size() <= 4; // Currently allow for games with 4 players
    }

    public void addPlayer(Player player) {
        if (this.canAddPlayer()) {
            this.players.add(player);
        }
        else {
            throw new ArrayIndexOutOfBoundsException("Game is already full.");
        }
    }


    /**
     * return the next player and keep moving
     * (I understand this is basically a Queue, but I can't get JPA to accept a Queue)
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

    private void setPlayer(int id, Player player) {
        Field field = board.getField(id);
        Figure figure = field.getFigure();
        figure.setPlayer(player);
        player.addFigure(figure);
    }

    public void letPlayersChangeCard() {
        // Let partners change one card
    }

    public void distributeCards(int cardNum) {
        for (Player player : players) {
            List<Card> cards = this.deck.deal(cardNum);
            player.setHand(cards);
        }
    }

    // BigRound is from 6 cards to 2 cards
    public void handSize() {
        int cardNum = 6;
        while (cardNum >= 2) {
            playRound(cardNum);
            cardNum--;
        }
    }

    // Round is until no player has a card left
    public void playRound(int cardNum) {
        for (Player player : players) {
            List<Card> cards = this.deck.deal(cardNum);
            player.setHand(cards);
        }


        this.letPlayersChangeCard();

        Player lastPlayer = players.get(players.size()-1);
        while (!lastPlayer.getHand().isEmpty()) {
            Player currentPlayer = this.getNextPlayer();
            playPlayersMove(currentPlayer);
        }
    }

    // playerMove is a players move
    public void playPlayersMove(Player currentPlayer) {
        //Choose figure and card
        //Get all possible moves
        //Choose move
        //Perform move
        //Check if finished
        //Check if won
    }

    public void play(){
        /*
        while (game not finished) {
            this.handSize();
        }
         */



    }

    public void endGame(){

    }

    public Boolean checkIfFinished(Player currentPlayer){
        return board.checkIfAllTargetFieldsOccupied(id, currentPlayer);
    }
    public Boolean checkIfWon(){
        Player Partner = this.players.get(1);
        return board.checkIfAllTargetFieldsOccupied(id, Partner);
    }

}
