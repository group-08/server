package ch.uzh.ifi.seal.soprafs20.cards;


import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table( name = "JokerCard")
public class JokerCard extends Card {

    @Enumerated
    private Value value = null;

    public void setValue(Value value){
        try {
            this.value = value;
        }catch (Exception jokerValueException){
            System.out.printf("invalid new value " + value + " for the Joker");
        }
    }

    public void unsetValue() {
        this.value = null;
    }

    public Value getValue() {
        return this.value;
    }

    public String toString(){
        if (this.value != null ) {
            return "Value: " + this.value;
        }else {
            return "Joker";
        }
    }
}
