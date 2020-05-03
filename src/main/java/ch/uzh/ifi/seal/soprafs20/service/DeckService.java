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

    ///default for 4 players if we change this we would ned a player list somewhere which has than a length
    private int numberOfPlayers = 4;

    public Deck getDeck(long Id){

        return deckRepository.findById(Id).get();
        ///by id
    }



////maybe done
    public List<Card> getCards(Long  Id){

        return getDeck(Id).getCardsInDeck();
    }
/////maybe done
    public void shuffleDeck(Long  Id){

        ///safe
        Collections.shuffle(getCards(Id));
    }

    private int checkLengthOfTheDeck(Long Id){
        return getDeck(Id).getCardsInDeck().size() * numberOfPlayers;
    }

    private void safeDeck(Deck deck){
        this.deckRepository.saveAndFlush(deck);

    }

    ///empty the deck
    private void emptyDeck(Long Id){
        this.getDeck(Id).setCards(null);
    }

    private void refillDeck(Long Id){

        ///empty the deck
        emptyDeck(Id);

        ///fill the deck with new cards
        List<Card> allCards = createCards();
        getDeck(Id).setCards(allCards);

        ///shuffle the Deck
        shuffleDeck(Id);

        ///safe the newly filled deck
        safeDeck(getDeck(Id));
    }



    public void checkIfEnoughCardsLeft( int amountToDraw, Long Id) {

        if( ! (amountToDraw * numberOfPlayers <= checkLengthOfTheDeck(Id))){
            refillDeck(Id); }
    }




    /// second version
    public List<Card> drawCards (int amountToDraw, Long  Id){


        List<Card> hand = new ArrayList<Card>();

        Card topCard;

        List<Card> currentCards = getCards(Id);

        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amountToDraw; loopVal++){
            ///from list instead
            topCard = currentCards.remove(0);
            hand.add(topCard);
        }

        emptyDeck(Id);
        getDeck(Id).setCards(currentCards);

        safeDeck(getDeck(Id));

        return hand;
    }


    public void createDeck(Deck deck){
        ///create the cards
        List<Card> allCards = createCards();

        //safe the cards
        for (Card card : allCards){
            cardService.addCard(card); }

        //// set the field
        deck.setCards(allCards);

        ///shuffle the deck

        shuffleDeck(deck.getId());

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
