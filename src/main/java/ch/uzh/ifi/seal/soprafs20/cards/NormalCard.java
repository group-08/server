package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;

@Entity
@Table( name = "NormalCard")
public class NormalCard extends Card {


    public NormalCard(){}

    public NormalCard(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
        this.remainingSteps = 0;
    }

    public String toString(){
        return String.format("Suit: %s Value: %s", this.suit, this.value);
    }

    @Override
    public Suit getSuit() {
        return suit;
    }


    @Override
    public Value getValue(){
        return this.value;
    }

    public void setValue(Value value)  {
        this.value = value;
    }

    @Override
    public int getRemainingSteps() {
        return remainingSteps;
    }

    @Override
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

    @Override
    public int hashCode() {
        int result = 17;

        result = (int) (31 * result + suit.hashCode());
        result = (int) (31 * result + value.hashCode());
        return result;
    }

}
