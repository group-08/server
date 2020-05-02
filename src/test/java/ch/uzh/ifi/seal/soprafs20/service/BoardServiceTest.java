package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private GameService gameService;

    private Board testBoard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testBoard = new Board();


        // when -> any object is being save in the gameRepository -> return the dummy testUser
        Mockito.when(boardRepository.save(Mockito.any())).thenReturn(testBoard);

}