package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Deck;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private DeckService deckService;

    private Deck testDeck;

    @BeforeEach
    public void setup() {

        testDeck = new Deck();
        ////fills deck with cards
        deckService.createDeck(testDeck);

    }


}