package ch.uzh.ifi.seal.soprafs20.cards;


import javax.persistence.Enumerated;


public class JokerCard extends Card {

    @Enumerated
    private Suit suit = null;

    @Enumerated
    private Value value = null;

    public void transformInto( Value value){
        try {
            this.value = value;
        }catch (Exception jokervalueexeption){
            System.out.printf("invalid new value " + value + " for the Joker");
            return;
        }
        this.suit = Suit.SPADES;

    }

    public String toString(){
        if (this.suit != null && this.value != null ) {
            return "Suit: " + this.suit + " Value: " + this.value;
        }else {
            return "Joker";
        }
    }
}
