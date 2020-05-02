package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private DeckService deckService;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    private void setup(){

    }

}