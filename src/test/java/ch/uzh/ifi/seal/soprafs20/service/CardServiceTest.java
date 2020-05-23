package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@SpringBootTest
class CardServiceTest {

    @Autowired
    private DeckService deckService;

    @Autowired
    private PlayerService playerService;

    private Card testCard;

    @Test
    void removeCard(){
        List<Card> hand = new ArrayList<>();
        Card cardKing = new NormalCard(Suit.CLUBS, Value.KING);
        Card cardFive = new NormalCard(Suit.CLUBS, Value.FIVE);
        hand.add(cardKing);
        hand.add(cardFive);
        Card cardKingNew = cardKing;
        playerService.removeCardFromHand(hand, cardKingNew);
        Assertions.assertEquals(1, hand.size());
        Assertions.assertTrue(hand.contains(cardFive));


    }

}