package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringBootTest
public class DeckServiceIntegrationTest {

    @Qualifier("deckRepository")
    @Autowired
    private DeckRepository deckRepository;

    @Qualifier("cardRepository")
    private CardRepository cardRepository;

    @Autowired
    private DeckService deckService;

    @BeforeEach
    public void setup() {
        deckRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        deckRepository.deleteAll();
        cardRepository.deleteAll();
    }




}
