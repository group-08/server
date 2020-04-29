package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DeckService {

    private final DeckRepository deckRepository;

    @Autowired
    public DeckService(@Qualifier("deckRepository") DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }


    private Deck getDeck(){

        return (Deck) deckRepository.findAll();
    }
/////not done
    private List<Card> getCards(){

        List<Card> cards = getDeck().getCards();
        return cards;
    }
/////not done
    private void shuffleDeck(){

        Deck deck = getDeck();
        Collections.shuffle(getCards());
        deckRepository.deleteInBatch(deckRepository.findAll());
        deckRepository.saveAndFlush(deck);
    }
}
