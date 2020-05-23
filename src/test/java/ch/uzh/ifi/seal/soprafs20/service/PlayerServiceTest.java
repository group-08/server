package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void removeFromHand_Test() {
        // create testplayer
        testUser = new User();

        // create player
        Player testPlayer = new Player();
        testPlayer.setUser(testUser);

        // create testgame
        Game testGame = new Game(testUser, "testGame");

        Mockito.when(playerRepository.getOne(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(playerRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testPlayer));

        // create testCards
        Card testCardTWO = new NormalCard(Suit.CLUBS, Value.TWO);
        Card testCardTHREE = new NormalCard(Suit.CLUBS, Value.THREE);
        Card testCardFOUR = new NormalCard(Suit.CLUBS, Value.FOUR);
        Card testCardFIVE = new NormalCard(Suit.CLUBS, Value.FIVE);
        Card testCardSIX = new NormalCard(Suit.CLUBS, Value.SIX);
        //Card testCardSEVEN = new NormalCard(Suit.CLUBS, Value.SEVEN);

        // Assign Cards to players hand
        List<Card> hand = new ArrayList<>();
        hand.add(testCardTWO);
        hand.add(testCardTHREE);
        hand.add(testCardFOUR);
        hand.add(testCardFIVE);
        hand.add(testCardSIX);
        //hand.add(testCardSEVEN);
        testPlayer.setHand(hand);

        List<Card> test = new ArrayList<>();
        test.add(testCardTWO);
        test.add(testCardTHREE);
        test.add(testCardFOUR);
        test.add(testCardSIX);
        //test.add(testCardSEVEN);

        // remove Cards from player
        playerService.removeFromHand(testPlayer, testCardFIVE);
        assertEquals(test, testPlayer.getHand());

        playerService.removeAllFromHand(testPlayer);
        assertEquals(0, testPlayer.getHand().size());
    }

}
