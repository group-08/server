package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setup() {
        gameRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
    }

    @Test
    public void SetUpGameIntegrationTest() {
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");

        User user4 = new User();
        user4.setUsername("firstname.lastnam");
        user4.setEmail("firstname@lastname.c");
        user4.setPassword("test");
        user4.setToken("assdf");
        user4.setStatus(UserStatus.ONLINE);

        Player testPlayer4 = new Player();
        testPlayer4.setUser(user4);

        User user2 = new User();
        user2.setUsername("firstname.lastna");
        user2.setEmail("firstname@lastname.");
        user2.setPassword("test");
        user2.setToken("fdsas");
        user2.setStatus(UserStatus.ONLINE);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(user2);

        User user3 = new User();
        user3.setUsername("firstname.lastn");
        user3.setEmail("firstname@lastname");
        user3.setPassword("test");
        user3.setToken("öklh");
        user3.setStatus(UserStatus.ONLINE);

        Player testPlayer3 = new Player();
        testPlayer3.setUser(user3);

        game.getPlayers().add(testPlayer2);
        game.getPlayers().add(testPlayer3);
        game.getPlayers().add(testPlayer4);

        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        userRepository.saveAndFlush(user3);
        userRepository.saveAndFlush(user4);
        gameRepository.saveAndFlush(game);

        deckService.createDeck(game.getDeck());

        gameService.setUpGame(game.getId());
        game = gameRepository.findById(game.getId()).orElse(null);
        assert game != null;


        // Check if player have the right colours
        assertEquals(Colour.BLUE, game.getPlayer(0).getColour());
        assertEquals(Colour.GREEN, game.getPlayer(1).getColour());
        assertEquals(Colour.YELLOW, game.getPlayer(2).getColour());
        assertEquals(Colour.RED, game.getPlayer(3).getColour());

        // Check if every player has 4 figures
        assertEquals(4, game.getPlayer(0).getFigures().size());
        assertEquals(4, game.getPlayer(1).getFigures().size());
        assertEquals(4, game.getPlayer(2).getFigures().size());
        assertEquals(4, game.getPlayer(3).getFigures().size());


        // Check if Figures player is the right player
        assertEquals(game.getPlayer(0), game.getPlayer(0).getFigures().get(0).getPlayer());

        // Check if Field 81's Figure is the first figure of the first player
        assertEquals(game.getPlayer(0).getFigures().get(0), game.getBoard().getField(81).getOccupant());

        // Check if Deck has 32 cards after distribution
        assertEquals(32, game.getDeck().getCardsInDeck().size());

        // Check if cardNum gets decreased after inital distribution
        assertEquals(5, game.getCardNum());

        // Check if exchangeCard boolean is set to true
        assertTrue(game.getExchangeCard());

        // Check if game state is set to running
        assertEquals(GameState.RUNNING, game.getGameState());

        // Check if every player has 6 cards
        assertEquals(6, game.getPlayer(0).getHand().size());
        assertEquals(6, game.getPlayer(1).getHand().size());
        assertEquals(6, game.getPlayer(2).getHand().size());
        assertEquals(6, game.getPlayer(3).getHand().size());
    }

    @Test
    public void PlayPlayersMoveTest() {

        //////////// GAME SETUP //////////
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");

        User user4 = new User();
        user4.setUsername("firstname.lastnam");
        user4.setEmail("firstname@lastname.c");
        user4.setPassword("test");
        user4.setToken("assdf");
        user4.setStatus(UserStatus.ONLINE);

        Player testPlayer4 = new Player();
        testPlayer4.setUser(user4);

        User user2 = new User();
        user2.setUsername("firstname.lastna");
        user2.setEmail("firstname@lastname.");
        user2.setPassword("test");
        user2.setToken("fdsas");
        user2.setStatus(UserStatus.ONLINE);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(user2);

        User user3 = new User();
        user3.setUsername("firstname.lastn");
        user3.setEmail("firstname@lastname");
        user3.setPassword("test");
        user3.setToken("öklh");
        user3.setStatus(UserStatus.ONLINE);

        Player testPlayer3 = new Player();
        testPlayer3.setUser(user3);

        game.getPlayers().add(testPlayer2);
        game.getPlayers().add(testPlayer3);
        game.getPlayers().add(testPlayer4);

        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        userRepository.saveAndFlush(user3);
        userRepository.saveAndFlush(user4);
        gameRepository.saveAndFlush(game);

        deckService.createDeck(game.getDeck());

        gameService.setUpGame(game.getId());

        game = gameRepository.findById(game.getId()).orElse(null);
        assert game != null;


        ////////// MOVE SETUP ///////////


        Card KingClubs = new NormalCard(Suit.CLUBS, Value.KING);

        MovePostDTO move = new MovePostDTO();
        move.setId(game.getId());
        move.setCard(KingClubs);
        // Sorry folgendes isch unschön
        move.setFigure(game.getPlayers().get(0).getFigures().get(0));
        move.setTargetField(game.getBoard().getField(1));



        /////////// MAKE MOVE ////////////



        // Move first figure of first player out to the StartField
        gameService.playPlayersMove(move);


        ////////// TEST MOVE ////////////
        

        // Here follow assertions if move executed correctly

    }

}
