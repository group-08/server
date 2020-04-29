package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private String id;

    @OneToMany(targetEntity = Card.class)
    List<Card> cards = new ArrayList<Card>();


    @Autowired
    private DeckRepository deckRepository;

    Deck(List<Card> cards){
        this.id = UUID.randomUUID().toString();
        this.cards = createDeck();

    }

    ///number of decks we play with default 2
    private final int numberOfDecks = 2;

    /// deals cards implemented as singleton
    public List<Card> deal(int amount){

        if (deckRepository.findAll().isEmpty()) {
            createDeck();
        }
        return createHand(amount);
    }

    ///drawing cards from the deck eg. poping
    private List<Card> createHand (int amount){

        List<Card> hand = new ArrayList<Card>();

        Card topCard;

        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amount; loopVal++){
            topCard = cards.remove(0);
            hand.add(topCard);
        }

        Deck currentDeck = (Deck) deckRepository.findAll();
        currentDeck.cards

        return hand;
    }

    ///creating the unique deck in two steps
    private List<Card> createDeck(){
            createDeckNormalPart();
            createJokers();
            Deck originalDeck = new Deck(cards);
            deckRepository.saveAndFlush(originalDeck);
            return originalDeck;
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



    public List<Card> getCards(){
        return cards;
    }


}
