package ch.uzh.ifi.seal.soprafs20.board;



import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.*;


import java.util.ArrayList;
import java.util.Collection;

public abstract class Board {

    private Collection<Field> fields;
    private ArrayList<Player> players;
    protected int version;

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

    /**
     * This checks if there are any savezones involved in this move and if the move is possible
     * @param currentField the field the figure is on now
     * @param targetField the field the figure wants to move on
     * @return boolean if move is possible
     */
    public boolean isMovePossible(Field currentField, Field targetField) {
        if (currentField instanceof GoalField) {
            // check if there is another figure in the goalfields before
        } else if (currentField instanceof HomeField) {
            // check if any field is homefield
        } else if (currentField instanceof FirstField) {
            // If occupied the first time, save and lock
        } else {
            return true;
        }
    }

    public void getPossibleTargetFields(Figure figure, Card card) {
        Field currentField = this.getCurrentField(figure);
        // gets all the possible moves from Adj. List, gets ID's of the fields and returns them as a list
    }

    public void move(Figure figure, Field targetField) {
        Field currentField = this.getCurrentField(figure);
        if (targetField.getOccupant() != null) {
            Figure occupant = targetField.getOccupant();
            this.sendFigureHome(occupant);
            currentField.setOccupant(null);
            targetField.setOccupant(figure);
        }
        else {
            currentField.setOccupant(null);
            targetField.setOccupant(figure);
        }

        /* Old Version
        Field currentField = this.getCurrentField(figure);
        Field targetField = this.getTargetField(currentField, card);
        if (this.isMovePossible(currentField, targetField)) {
            if (targetField.getOccupant() != null) {
                Figure occupant = targetField.getOccupant();
                this.sendFigureHome(occupant);
                currentField.setOccupant(null);
                targetField.setOccupant(figure);
            }
            else {
                currentField.setOccupant(null);
                targetField.setOccupant(figure);
            }
        } else {
            // throw error message
        } */
    }

}
