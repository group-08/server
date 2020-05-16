package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class BoardServiceIntegrationTest {

    @Qualifier("boardRepository")
    @Autowired
    private BoardRepository boardRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;





    private Game game;

    @BeforeEach
    public void init() {
        gameRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        boardRepository.deleteAll();
    }

    @Test
    public void setUpBoard() {
        Board board = new Board();
        for (Field field : board.getFields())   {
            if (field.getOccupant() != null)    {
                assertEquals(field, field.getOccupant().getField());
            }
        }
    }

    @Test
    public void boardIntegrationTest() {
        Board board = new Board();
        boardRepository.saveAndFlush(board);
        Board afterBoard = boardRepository.findById(board.getId()).orElse(null);
        assert afterBoard != null;
        for (Field field : afterBoard.getFields())   {
            if (field.getOccupant() != null)    {
                assertEquals(field, field.getOccupant().getField());
            }
        }
    }

    @Test
    public void blowAwayFigureTests(){
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        this.game = new Game(user, "testGame");
        gameRepository.saveAndFlush(game);

        gameService.setUpGame(game.getId());

        userRepository.saveAndFlush(user);

        gameRepository.saveAndFlush(game);
    }
}
