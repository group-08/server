package ch.uzh.ifi.seal.soprafs20.board;



import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;


import java.util.ArrayList;
import java.util.Collection;

public abstract class Board {

    private Collection<Field> fields;
    private ArrayList<Player> players;
    protected int version;

    public void play(){
        /* something */
    }

    /**
     * Gets a currentField as well as a Card and returns the targetField
     * @param currentField Current Field the figure is on
     * @param card Card to be played
     * @return the field the figure will land on
     */
    public Field getTargetField(Field currentField, Card card) {
        /* takes value of card, adds it to current field and returns target field */
    }

    /**
     * Takes a Figure and gets Figures current Field
     * @param figure figure you want the current field of
     * @return the current field of the figure
     */
    public Field getCurrentField(Figure figure) {
        /* loop through all the fields and check if occupant matches figure */
        for (Field field: fields) {
            if (field.getOccupant() == figure) {
                return field;
            }
        }
        return null;
    }

    /**
     * Sends the figure home
     * @param figure that has to be sent home
     */
    public void sendFigureHome(Figure figure) {
        /* sends figure home */
    }



}
