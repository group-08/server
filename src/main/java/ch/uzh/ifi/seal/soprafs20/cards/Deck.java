package ch.uzh.ifi.seal.soprafs20.cards;

import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static ch.uzh.ifi.seal.soprafs20.cards.Value.*;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(targetEntity = Card.class)
    List<Card> cards = new ArrayList<Card>();

    private final int numberOfDecks = 2;

    public List<Card> deal(int amount){

        if (cards.isEmpty()) {
            createDeck();
            shuffle(cards);
            deal(amount);
        }else {
            List<Card> hand = new ArrayList<Card>();

            for (int loopVal = 0; loopVal < amount; loopVal++){
                hand.add(cards.remove(0));
            }

            return hand;
        }


    }

    private void createDeck(){
            createDeckNormalPart();
            createJokers();
    }


    private void createDeckNormalPart(){
        for(Suit suit : Suit.values()){
            for(Value myValue : Value.values()){
                cards.add(new NormalCard(suit, myValue));
            }
        }
    }

    private void createJokers(){
        int numberOfJokers = 2 * numberOfDecks;
        int iter = 0;
        while (iter < numberOfJokers ){
            cards.add(new JokerCard());
        }
    }


    private void shuffle(List<Card> cards){
        Collections.shuffle(this.cards);
    }


}
