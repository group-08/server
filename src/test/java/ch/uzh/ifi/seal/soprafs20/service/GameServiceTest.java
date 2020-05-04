package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private DeckService deckService;

    @InjectMocks
    private BoardService boardService;

    private User user;
    private User user2;
    private User user3;
    private User user4;

    private Game game;

    private Player testPlayer;
    private Player testPlayer2;
    private Player testPlayer3;
    private Player testPlayer4;

    @BeforeEach
    private void setup(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void initGame_Test(){
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");

        assertEquals(6, game.getCardNum());
        assertEquals(user, game.getHost());
        assertEquals(testPlayer.getUser().getUsername(), game.getPlayer(0).getUser().getUsername());
        assertEquals(user.getUsername(), game.getHost().getUsername());
        assertEquals(1, game.getPlayers().size());
        assertEquals(GameState.PENDING, game.getGameState());
        assertNotNull(game.getDeck());
        assertNotNull(game.getBoard());
    }

    @Test
    public void setUpGame_test(){

        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");

        User user4 = new User();
        user4.setUsername("firstname.lastnam");
        user4.setEmail("firstname@lastname.c");
        user4.setStatus(UserStatus.ONLINE);

        Player testPlayer4 = new Player();
        testPlayer4.setUser(user4);

        User user2 = new User();
        user2.setUsername("firstname.lastna");
        user2.setEmail("firstname@lastname.");
        user2.setStatus(UserStatus.ONLINE);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(user2);

        User user3 = new User();
        user3.setUsername("firstname.lastn");
        user3.setEmail("firstname@lastname");
        user3.setStatus(UserStatus.ONLINE);

        Player testPlayer3 = new Player();
        testPlayer3.setUser(user3);

        Card card = new NormalCard(Suit.CLUBS, Value.SEVEN);
        Deck deck = new Deck();

        given(gameRepository.findById((Mockito.anyLong()))).willReturn(java.util.Optional.of(game));
        given(gameRepository.save(Mockito.any())).willReturn(game);
        given(cardRepository.saveAndFlush(card)).willReturn(card);
        given(deckRepository.saveAndFlush(deck)).willReturn(deck);
        given(deckRepository.findById(deck.getId())).willReturn(java.util.Optional.of(deck));

        game.getPlayers().add(testPlayer2);
        game.getPlayers().add(testPlayer3);
        game.getPlayers().add(testPlayer4);

        gameService.setUpGame(1L);
    }
}