package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(targetEntity = GameState.class)
    GameState gameState;

    @OneToMany(targetEntity = Player.class)
    ArrayList<Player> players;

    @OneToOne(targetEntity = Board.class)
    Board board;

    @OneToOne(targetEntity = Deck.class)
    Deck deck;

    @ManyToOne(targetEntity = WeatherState.class)
    WeatherState weatherState;

    Game(){

    }


    public void initialize(){


    }

    public void startGame(){

    }

    public Player getNextPlayer(){
        return null;
    }

    public void endGame(){

    }

    public void play(){

    }

    public void move(Card card, Player player){

    }
}
