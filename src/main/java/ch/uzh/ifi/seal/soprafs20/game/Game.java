package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.board.CasualBoard;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.field.Field;


import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Array;
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

    @Enumerated
    WeatherState weatherState;

    Game(){
        this.board = new CasualBoard();
        this.deck = new Deck();
        this.gameState = GameState.PENDING;
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

    public void startGame(){
        // Mix up the players
        Collections.shuffle(this.players);

        // Shuffle the cards
        this.deck.shuffle();

        // Set the gameState to running
        this.gameState = GameState.RUNNING;

        // Assign figures to players
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
    public void playBigRound() {
        int cardNum = 6;
        while (cardNum >= 2) {
            playSmallRound(cardNum);
            cardNum--;
        }
    }

    // SmallRound is if every player played once
    public void playSmallRound(int cardNum) {
        for (Player player : players) {
            List<Card> cards = this.deck.deal(cardNum);
            player.setHand(cards);
        }

        this.letPlayersChangeCard();

        for (int i = 0; i < 4; i++) {
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
            this.playBigRound();
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
