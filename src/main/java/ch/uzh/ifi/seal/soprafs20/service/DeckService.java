package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
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
    public DeckService(@Qualifier("deckRepository") DeckRepository deckRepository, @Qualifier("cardRepository") CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardService = new CardService(cardRepository);
    }

    ///number of decks we play with default 1
    private int numberOfDecks = 1;

    ///default for 4 players if we change this we would ned a player list somewhere which has than a length
    private int numberOfPlayers = 4;

    public Deck getDeck(long Id){
        return deckRepository.findById(Id).orElse(null);
        ///by id
    }



////maybe done
    public List<Card> getCards(Long  Id){

        return getDeck(Id).getCardsInDeck();
    }
/////maybe done
    public void shuffleDeck(Deck deck){

        Collections.shuffle(deck.getCardsInDeck());
    }

    public int checkLengthOfTheDeck(long Id){
        return getDeck(Id).getCardsInDeck().size();
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

        Deck deck = getDeck(Id);

        ///shuffle the Deck
        shuffleDeck(deck);

        ///safe the newly filled deck
        safeDeck(getDeck(Id));
    }



    public void checkIfEnoughCardsLeft( int amountToDraw, Long Id) {

        if( ! (amountToDraw * numberOfPlayers <= checkLengthOfTheDeck(Id))){
            refillDeck(Id); }
    }


    public Card removeCard(Long id) {
        return getDeck(id).getCardsInDeck().remove(0);
    }

    /// second version
    public List<Card> drawCards (int amountToDraw, Long  Id){

        List<Card> hand = new ArrayList<Card>();


        ///drawing as many cards from cards as necessary
        for (int loopVal = 0; loopVal < amountToDraw; loopVal++){
            ///from list instead
            hand.add(removeCard(Id));
        }

        return hand;
    }


    public void createDeck(Deck deck){
        ///create the cards
        List<Card> allCards = this.createCards();

        // Shuffle the cards
        Collections.shuffle(allCards);

        //safe the cards
        for (Card card : allCards){
            cardService.addCard(card); }

        //// set the field
        deck.setCards(allCards);

        ///shuffle the deck
        this.shuffleDeck(deck);

        ///safe the deck in jpa
        this.safeDeck(deck);

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
