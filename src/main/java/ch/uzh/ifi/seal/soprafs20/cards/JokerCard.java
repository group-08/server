package ch.uzh.ifi.seal.soprafs20.cards;


import javax.persistence.Enumerated;


public class JokerCard extends Card {


    @Enumerated
    private Value value = null;



    public void transformInto( Value value){
        try {
            this.value = value;
        }catch (Exception jokerValueException){
            System.out.printf("invalid new value " + value + " for the Joker");
            return;
        }

    }


    public String toString(){
        if (this.value != null ) {
            return "Value: " + this.value;
        }else {
            return "Joker";
        }
    }
}
