package ch.uzh.ifi.seal.soprafs20.cards;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Card.class, cascade = CascadeType.ALL)
    List<Card> cards = new ArrayList<Card>();



    public Deck(){
        this.cards = new ArrayList<Card>();

    }

/*
    ///number of decks we play with default 2
    private final int numberOfDecks = 2;


    /// deals cards implemented as singleton
    public List<Card> deal(int amount){

        if (deckRepository.findAll().isEmpty()) {
            createCards();
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


        return hand;
    }

    ///creating the unique deck in two steps
    private List<Card> createCards(){
        List<Card> originalCards = new ArrayList<>()
            createCardsNormalPart(originalCards);
            createJokers(originalCards);

            return originalDeck;

    }

    ///create normal cards by iterating through the values and suits
    private void createCardsNormalPart(List<Card> originalCards){
        for(Suit suit : Suit.values()){
            for(Value myValue : Value.values()){
                Card newNormalCard = new NormalCard(suit, myValue);
                originalCards.add(newNormalCard);
            }
        }
    }

    ///creating Joker cards separately
    private void createJokers(List<Card> originalCards){
        int numberOfJokers = 2 * numberOfDecks;
        int iter = 0;
        while (iter < numberOfJokers ){
            originalCards.add(new JokerCard());
            iter++;
        }
    }
*/



    public List<Card> getCardsInDeck() {
        return this.cards;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    public Long getId(){
        return this.id;
    }
}
