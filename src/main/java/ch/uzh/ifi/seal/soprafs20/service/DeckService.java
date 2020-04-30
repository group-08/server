package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class DeckService {

    private final DeckRepository deckRepository;


    @Autowired
    public DeckService(@Qualifier("deckRepository") DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    private Deck createDeck(Deck deck){
        deckRepository.saveAndFlush(deck);
        return deck;
    }


    private Deck getDeck(){

        return (Deck) this.deckRepository.findAll();
    }

////maybe done
    private List<Card> getCards(){

        return getDeck().getCardsInDeck();
    }
/////maybe done
    private void shuffleDeck(){

        Collections.shuffle(getCards());
    }

    private int checkLengthOfTheDeck(){
        return getDeck().getCardsInDeck().size();
    }

    private void safeDeck(Deck deck){
        this.deckRepository.saveAndFlush(deck);

    }

    private void refillDeck(){
        getDeck().setCards(null);
        createCards(getDeck());
        this.deckRepository.saveAndFlush(getDeck());
    }

    ///number of decks we play with default 2
    private final int numberOfDecks = 2;


    /// deals cards implemented as singleton
    public List<Card> deal(int amount){

        return createHand(amount);
    }

    ///drawing cards from the deck eg. poping
    private List<Card> createHand (int amount){

        List<Card> hand = new ArrayList<Card>();

        Card topCard;

        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amount; loopVal++){
            topCard = getCards().remove(0);
            topCard = cards.remove(0);
            hand.add(topCard);
        }


        return hand;
    }

    ///creating the unique deck in two steps
    private void createCards(Deck deck){
        List<Card> originalCards = new ArrayList<>();
        createCardsNormalPart(originalCards);
        createJokers(originalCards);
        deck.setCards(originalCards);
        deckRepository.saveAndFlush(deck);

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
}
