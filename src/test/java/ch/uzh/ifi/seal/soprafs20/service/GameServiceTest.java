package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.DeckRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//@SpringBootTest
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    private Game game;

    private Player testPlayer1;
    private Player testPlayer2;
    private Player testPlayer3;
    private Player testPlayer4;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        int leaderBoardScore1 = 0;
        int leaderBoardScore2 = 1;


        // given
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@Name.tld");
        user1.setUsername("testUsername");
        user1.setLeaderBoardScore(leaderBoardScore1);
        user1.setToken("token1");

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("test@Name.tld");
        user2.setUsername("testUsername");
        user2.setLeaderBoardScore(leaderBoardScore2);
        user2.setToken("token2");

        testPlayer1 = new Player();
        testPlayer1.setUser(user1);
        testPlayer1.setId(user1.getId());

        testPlayer2 = new Player();
        testPlayer2.setUser(user2);
        testPlayer2.setId(user2.getId());

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
    public void increaseScoreTest(){


        Mockito.when(userRepository.findById(1l)).thenReturn(java.util.Optional.ofNullable(user1));
        Mockito.when(userRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(user2));

        Mockito.when(userRepository.saveAndFlush(user1)).thenReturn(user1);
        Mockito.when(userRepository.saveAndFlush(user2)).thenReturn(user2);

        Mockito.when(userService.getUserById(testPlayer1.getId())).thenReturn(user1);
        Mockito.when(userService.getUserById(testPlayer2.getId())).thenReturn(user2);

        assert user1 != null;
        assert user2 != null;

        gameService.increaseScore(testPlayer1, testPlayer2);

        Assertions.assertEquals(1, user1.getLeaderBoardScore());
        Assertions.assertEquals(2, user2.getLeaderBoardScore());

    }

    public boolean checkToken(Long gameId, String tokenToCheck){
        Game actualGameToCheck = gameRepository.findById(gameId).get();
        User Host = actualGameToCheck.getHost();
        User UserBelongingToToken = userService.getUserByToken(tokenToCheck);
        return Host == UserBelongingToToken;
    }


    @Test
    public void checkTokenTest(){
        long gameID = 1L;
        Game game = new Game();
        game.setHost(user1);


        Mockito.when(gameRepository.findById(gameID)).thenReturn(java.util.Optional.of((game)));
        assert game != null;

        Mockito.when(userRepository.findByToken(user1.getToken())).thenReturn(user1);
        Mockito.when(userRepository.findByToken(user2.getToken())).thenReturn(user2);


        assertTrue(gameService.checkToken(gameID, user1.getToken()));
        assertFalse(gameService.checkToken(gameID, user2.getToken()));

    }
}

