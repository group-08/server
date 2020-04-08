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
        /* check if targetfield is safezone, if so, make sure that if it is a goalfield, there's no other
        figure in the goalzone in between, if it is the first field and occupied, not possible, if on the way there is
        a first field that is occupied and blocking not possible either. Furthermore checks if targetfield or any field
        in between is not a home field.
         */
        return false;
    }

    public void move(Card card, Player player, Figure figure){
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
        }
    }

}
