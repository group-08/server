package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CardTest {



    @Test
    void createNormalCard() {
        // given
        Value valueTestCard = Value.ACE;
        Suit suitTestCard = Suit.CLUBS;
        Card testCard = new NormalCard(suitTestCard,valueTestCard);

        // assert
        assertNotNull(testCard);
    }

    @Test
    void createNormalCardWithSuitAndValue() {
        // given
        Card testCard = new NormalCard(Suit.DIAMONDS, Value.EIGHT);

        // assert
        assertEquals(Value.EIGHT, testCard.getValue());
    }
}
