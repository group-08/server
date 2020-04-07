package ch.uzh.ifi.seal.soprafs20.game;



import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.field.Field;


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
        return null;
    }

    public void endGame(){

    }

    public void play(){

    }

    public void move(Card card, Player player, Figure figure){
        Field currentField = this.board.getCurrentField(figure);
        Field targetField = this.board.getTargetField(currentField, card);
        if (this.board.isMovePossible(currentField, targetField)) {
            if (targetField.getOccupant() != null) {
                Figure occupant = targetField.getOccupant();
                this.board.sendFigureHome(occupant);
                currentField.setOccupant(null);
                targetField.setOccupant(figure);
            }
            else {
                currentField.setOccupant(null);
                targetField.setOccupant(figure);
            }
        } else {
            // throw error message
        }
    }
}
