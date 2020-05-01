package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DeckService {

    private final DeckRepository deckRepository;
    private final CardService cardService;


    @Autowired
    public DeckService(@Qualifier("deckRepository") DeckRepository deckRepository, CardService cardService) {
        this.deckRepository = deckRepository;
        this.cardService = cardService;
    }

    ///number of decks we play with default 2
    private int numberOfDecks = 2;

    private Deck getDeck(){

        return (Deck) this.deckRepository.findAll();
        ///by id
    }

////maybe done
    private List<Card> getCards(){

        return getDeck().getCardsInDeck();
    }
/////maybe done
    private void shuffleDeck(){

        ///safe
        Collections.shuffle(getCards());
    }

    private int checkLengthOfTheDeck(){
        return getDeck().getCardsInDeck().size();
    }

    private void safeDeck(Deck deck){
        this.deckRepository.saveAndFlush(deck);

    }

    ///empty the deck
    private void emptyDeck(){
        this.getDeck().setCards(null);
    }

    private void refillDeck(){

        ///empty the deck
        emptyDeck();

        ///fill the deck with new cards
        List<Card> allCards = createCards();
        getDeck().setCards(allCards);

        ////Todo is this necessary?
        ///safe the newly filled deck
        safeDeck(getDeck());
    }



    private boolean checkIfEnoughCardsLeft(int amountToDraw) {

        return amountToDraw <= checkLengthOfTheDeck();
    }

    ///Todo which verison does it here?
    ///drawing cards from the deck eg. poping
    private List<Card> drawCards1 (int amountToDraw){

        List<Card> hand = new ArrayList<Card>();

        Card topCard;


        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amountToDraw; loopVal++){
            topCard = getCards().remove(0);
            hand.add(topCard); }

        return hand;
    }


    /// second version
    private List<Card> drawCard2 (int amount){

        List<Card> hand = new ArrayList<Card>();

        Card topCard;

        List<Card> currentCards = getCards();

        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amount; loopVal++){
            ///from list instead
            topCard = currentCards.remove(0);
            hand.add(topCard);
        }

        emptyDeck();
        getDeck().setCards(currentCards);

        safeDeck(getDeck());

        return hand;
    }


    private void createDeck(Deck deck){
        ///create the cards
        List<Card> allCards = createCards();

        //safe the cards
        for (Card card : allCards){
            cardService.addCard(card); }

        //// set the field
        deck.setCards(allCards);

        ///safe the deck in jpa
        safeDeck(deck);

    }
    ///creating the unique deck in two steps
    private List<Card> createCards(){

        ///creating a list with all cards
        List<Card> normalCards = createCardsNormalPart();
        List<Card> jokers = createJokers();
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(normalCards);
        allCards.addAll(jokers);
        return allCards;
    }

    ///create normal cards by iterating through the values and suits
    private List<Card> createCardsNormalPart(){
        List<Card> cards = new ArrayList<Card>();
        for(Suit suit : Suit.values()){
            for(Value myValue : Value.values()){
                Card newNormalCard = new NormalCard(suit, myValue);
                cards.add(newNormalCard);
            }
        }
        return cards;
    }

    ///creating Joker cards separately
    private List<Card> createJokers(){
        List<Card> jokers = new ArrayList<>();
        int numberOfJokers = 2 * numberOfDecks;
        int iter = 0;
        while (iter < numberOfJokers ){
            jokers.add(new JokerCard());
            iter++;
        }
        return jokers;
    }


}
