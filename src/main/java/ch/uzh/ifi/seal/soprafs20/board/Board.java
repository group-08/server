package ch.uzh.ifi.seal.soprafs20.board;



import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.*;


import java.util.ArrayList;
import java.util.Collection;

public abstract class Board {

    private Graph fields;
    private ArrayList<Player> players;
    protected int version;
    private Board board;

    public Graph getGraph() {
        return this.fields;
    }

    /**
     * Takes a Figure and gets Figures current Field
     * @param figure figure you want the current field of
     * @return the current field of the figure
     */
    public Field getCurrentField(Figure figure) {
        return figure.getField();
    }

    /**
     * Sends the figure home
     * @param figure that has to be sent home
     */
    public void sendFigureHome(Figure figure) {
        /* sends figure home */
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
        if (targetField instanceof FirstField && currentField instanceof HomeField) {
            ((FirstField) targetField).setBlocked(true);
        }
        if (currentField instanceof FirstField && !(targetField instanceof FirstField)) {
            ((FirstField) currentField).setBlocked(false);
        }
    }

    public ArrayList<Field> getPossibleFields(Card card, Field field) {
        return this.fields.getPossibleFields(card, field, fields);
    }

}
