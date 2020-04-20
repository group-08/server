package ch.uzh.ifi.seal.soprafs20.cards;

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
        return null;
    }

    private Deck createDeck(){
        if (cards.isEmpty()){
            createDeckNormalPart();
            createJokers();
            return
        }else {
            return deck;
        }
    }

    private void createDeckNormalPart(){
        for(Suit suit : Suit.values()){
            for(Value myValue : values()){
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


    private void shuffle(){
        Collections.shuffle(cards);
    }


}
