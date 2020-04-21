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


    ///number of decks we play with default 2
    private final int numberOfDecks = 2;

    /// deals cards implemented as singleton
    public List<Card> deal(int amount){

        if (cards.isEmpty()) {
            createDeck();
        }
        return createHand(amount);
    }

    ///drawing cards from the deck eg. poping
    private List<Card> createHand (int amount){

        List<Card> hand = new ArrayList<Card>();

        for (int loopVal = 0; loopVal < amount; loopVal++){
            hand.add(cards.remove(0));
        }

        return hand;
    }

    ///creating the unique deck in two steps
    private void createDeck(){
            createDeckNormalPart();
            createJokers();
    }

    ///create normal cards by iterating through the values and suits
    private void createDeckNormalPart(){
        for(Suit suit : Suit.values()){
            for(Value myValue : Value.values()){
                cards.add(new NormalCard(suit, myValue));
            }
        }
    }

    ///creating Joker cards separately
    private void createJokers(){
        int numberOfJokers = 2 * numberOfDecks;
        int iter = 0;
        while (iter < numberOfJokers ){
            cards.add(new JokerCard());
            iter++;
        }
    }

    ///shuffle the deck implemented as singleton which creates the deck if necessary
    public void shuffle(){
        if (cards.isEmpty()){
            createDeck();
        }
        Collections.shuffle(this.cards);
    }


}
