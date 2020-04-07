package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;

public class NormalCard extends Card {

    @Enumerated
    private Suit suit;

    @Enumerated
    private Value value;

    public String toString(){

       return String.format("Suit: %s Value: %s", this.suit, this.value);

    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }


}
