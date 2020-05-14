package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
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

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
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
}
