package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(targetEntity = Card.class)
    List<Card> cards = new ArrayList<Card>();


    @Autowired
    private DeckRepository deckRepository;

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

        Card topCard;

        for (int loopVal = 0; loopVal < amount; loopVal++){
            topCard = cards.remove(0);
            hand.add(topCard);
         /* here it we have two options we either push the topCard onto the DiscardPile immediately
         * which is technically wrong since there would be a backend and e frontend card duplicated
         * or when a card is played in the frontend we send it to the discardPile after that.*/

        }

        return hand;
    }

    ///creating the unique deck in two steps
    private void createDeck(){
            createDeckNormalPart();
            createJokers();
            deckRepository.saveAndFlush(cards);
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
