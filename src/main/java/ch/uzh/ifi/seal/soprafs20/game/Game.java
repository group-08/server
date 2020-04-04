package ch.uzh.ifi.seal.soprafs20.game;

import User.Player;
import board.Board;
import cards.Card;
import cards.Deck;

import java.util.ArrayList;

public class Game {


    Long id;
    GameState gameState;
    Player currentPlayer;
    ArrayList<Player> players;
    Board board;
    Deck deck;
    WeatherState weatherState;

    Game(){

    }


    public void initialize(){


    }

    public void startGame(){

    }

    public Player getNextPlayer(){

    }

    public void endGame(){

    }

    public void play(){

    }

    public void move(Card card, Player player){

    }
}
