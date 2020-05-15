package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {



    @Test
    public void createNormalCard() {
        // given
        Card testCard = new NormalCard();

        // assert
        assertEquals(testCard.getType(), CardType.Normal);
    }

    @Test
    public void createNormalCardWithSuitAndValue() {
        // given
        Card testCard = new NormalCard(Suit.DIAMONDS, Value.EIGHT);

        // assert
        assertEquals(testCard.getType(), CardType.Normal);
        assertEquals(testCard.getValue(), Value.EIGHT);
    }
}
