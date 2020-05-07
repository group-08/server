package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;

@Entity
@Table( name = "NormalCard")
public class NormalCard extends Card {

    @Enumerated
    private Suit suit;

    public NormalCard() {
        this.type = CardType.Normal;
    }

    public NormalCard(Suit suit, Value value) {
        this.type = CardType.Normal;
        this.suit = suit;
        this.value = value;
    }

    public String toString(){
        return String.format("Suit: %s Value: %s", this.suit, this.value);
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue(){
        return this.value;
    }
}
