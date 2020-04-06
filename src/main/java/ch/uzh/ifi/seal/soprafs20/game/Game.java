package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
