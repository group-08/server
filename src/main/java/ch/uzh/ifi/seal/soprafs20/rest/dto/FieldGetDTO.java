package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;

public class FieldGetDTO {

    private long id;

    private Figure occupant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Figure getOccupant() {
        return this.occupant;
    }

    public void setOccupant(Figure occupant) {
        this.occupant = occupant;
    }
}
