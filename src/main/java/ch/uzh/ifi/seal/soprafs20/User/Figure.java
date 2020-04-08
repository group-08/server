package ch.uzh.ifi.seal.soprafs20.User;

import ch.uzh.ifi.seal.soprafs20.field.Field;

public class Figure {
    Long id;
    Player player;
    Field field;


    public long getId(){
        return this.id;
    }

    public Player getPlayer() { return this.player; }

    public Field getField() { return this.field; }
}
