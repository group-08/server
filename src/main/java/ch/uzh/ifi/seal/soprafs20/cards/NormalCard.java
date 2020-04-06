package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;

public class NormalCard extends Card {

    @Enumerated
    private Suit suit;

    @Enumerated
    private Value value;

    public String toString(){
        //returns a String
        return null;
    }
}
