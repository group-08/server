package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.field.Field;

import java.io.Serializable;
import javax.persistence.*;

import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "BOARD")
public abstract class Board implements Serializable {


    @Id
    private long id;

    @OneToMany(targetEntity = Field.class)
    private Collection<Field> fields;

    @OneToMany(targetEntity = Player.class)
    private List<Player> players;

    private Graph graphFields;
    private ArrayList<Player> players2;
    protected int version;
    private Board board;

    public Graph getGraph() {
        return this.graphFields;
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
        return this.graphFields.getPossibleFields(card, field, graphFields);
    }

}
