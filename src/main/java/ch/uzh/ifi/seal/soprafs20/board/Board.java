package ch.uzh.ifi.seal.soprafs20.board;



import ch.uzh.ifi.seal.soprafs20.User.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Board {

    private Collection<Field> fields;
    private ArrayList<Player> players;
    protected int version;

    public void play(){
        /* something */
    }



}
