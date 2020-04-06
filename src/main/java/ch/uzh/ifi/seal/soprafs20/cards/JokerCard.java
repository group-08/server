package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.Enumerated;

public class JokerCard extends Card {

    @Enumerated
    private Suit suit = null;

    @Enumerated
    private Value value = null;

    public void transformInto(Suit suit, Value value){
        //maybe this should return something
    }

    public String toString(){
        //returns a String
        return null;
    }
}
