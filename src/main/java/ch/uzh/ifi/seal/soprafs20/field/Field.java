package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.User.Figure;

public abstract class Field {

    private Figure occupant;

    public Figure getOccupant() {
        return occupant;
    }

    public void setOccupant(Figure occupant){
        return;
    }
}
