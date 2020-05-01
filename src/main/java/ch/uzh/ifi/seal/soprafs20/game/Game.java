package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
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

    String name;

    @Enumerated
    WeatherState weatherState;

    public Game(User user, String name){
        this.board = new CasualBoard();
        this.name = name;
        this.deck = new Deck();
        this.gameState = GameState.PENDING;
        this.host = user;

        Player hostPlayer = new Player();
        hostPlayer.setUser(host);
        this.players.add(hostPlayer);
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
    public void setPlayer(int id, Player player) {
        if (board.getField(id).getFigure() != null) {
            Field field = board.getField(id);
            Figure figure = field.getFigure();
            figure.setPlayer(player);
            player.addFigure(figure);
        } else {
            // throw error
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getPlayer (int playerIndex) {
        return this.players.get(playerIndex);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }
}
