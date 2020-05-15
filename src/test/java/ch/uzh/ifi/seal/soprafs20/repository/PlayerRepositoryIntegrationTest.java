package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.user.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @AfterEach
    public void teardown() {
        playerRepository.deleteAll();
    }

    @Test
    public void getOnePlayerRepositoryTest() {
        //given
        User user = new User();
        user.setEmail("firstname@lastname.tld");
        user.setUsername("firstname.lastname");
        user.setPassword("test");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        Player player = new Player();
        player.setUser(user);
        player.setColour(Colour.BLUE);

        // create testCard
        NormalCard testCardTWO = new NormalCard(Suit.CLUBS, Value.TWO);

        // Assign Cards to players hand
        List<Card> hand = new ArrayList<>();
        hand.add(testCardTWO);
        player.setHand(hand);

        Figure figure = new Figure();
        figure.setPlayer(player);
        player.addFigure(figure);

        entityManager.persist(player);
        entityManager.persist(user);
        entityManager.flush();



        // when
        Player found = playerRepository.getOne(player.getId());

        // then
        assertEquals(found.getColour(), player.getColour());
        assertEquals(found.getUser().getUsername(), player.getUser().getUsername());
        assertEquals(found.getId(), player.getId());
        assertEquals(found.getHand(), player.getHand());
        assertEquals(found.getFigures(), player.getFigures());
    }
}
