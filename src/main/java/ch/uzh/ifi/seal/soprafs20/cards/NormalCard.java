package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;

@Entity
@Table( name = "NormalCard")
public class NormalCard extends Card {

    @Enumerated
    private Suit suit;

    @Column
    private int remainingSteps;

    public NormalCard() {
        this.type = CardType.Normal;
    }

    public NormalCard(Suit suit, Value value) {
        this.type = CardType.Normal;
        this.suit = suit;
        this.value = value;
        this.remainingSteps = 0;
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

    public void setValue(Value value)  {
        this.value = value;
    }

    public int getRemainingSteps() {
        return remainingSteps;
    }

    public void setRemainingSteps(int remainingSteps) {
        this.remainingSteps = remainingSteps;
    }

    @Override
    public boolean equals(Object otherObject){
        if(otherObject==null){
            return false;
        }
        if(otherObject instanceof NormalCard){
            NormalCard other = (NormalCard) otherObject;
            return suit.equals(other.suit) && value.equals(other.value);
        }
        return false;
    }


}
