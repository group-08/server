package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.field.GoalField;
import ch.uzh.ifi.seal.soprafs20.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;


import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.user.*;
import org.junit.jupiter.api.Test;


import java.util.List;

class BoardServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private BoardService boardService;

    @InjectMocks
    private GameService gameService;

    private User testUser1;

    private User testUser2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPossibleFields_Test() {

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setEmail("test@Name.tld");
        testUser1.setUsername("testUsername");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setEmail("test@Name");
        testUser2.setUsername("test2");

        Player testPlayer1 = new Player();
        testPlayer1.setUser(testUser1);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(testUser2);

        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

        Figure figure1 = new Figure();
        figure1.setPlayer(testPlayer1);

        Figure figure2 = new Figure();
        figure2.setPlayer(testPlayer2);

        long id = 0;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        Field field1 = testGame.getBoard().getField(1);
        field1.setOccupant(figure1);

        Field field0_samePlayer_as_GoalField = testGame.getBoard().getField(0);
        field0_samePlayer_as_GoalField.setOccupant(figure1);

        Field field63_not_samePlayer_as_GoalField = testGame.getBoard().getField(63);
        field63_not_samePlayer_as_GoalField.setOccupant(figure2);


        GoalField goalField1 = (GoalField) testGame.getBoard().getField(64);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(67);
        goalField1.setPlayer(testPlayer1);
        goalField2.setPlayer(testPlayer1);
        goalField3.setPlayer(testPlayer1);
        goalField4.setPlayer(testPlayer1);


        Card cardFIVE = new NormalCard(Suit.CLUBS, Value.FIVE);
        Card cardTEN = new NormalCard(Suit.CLUBS, Value.TEN);
        Card cardTWO = new NormalCard(Suit.CLUBS, Value.TWO);

        List<Field> possibleFieldsTWO =
                boardService.getPossibleFields(1L, cardTWO, field0_samePlayer_as_GoalField);
        List<Field> possibleFieldsTWO_diffPlayer =
                boardService.getPossibleFields(1L, cardTWO, field63_not_samePlayer_as_GoalField);
        List<Field> possibleFieldsFIVE = boardService.getPossibleFields(1L, cardFIVE, field1);
        List<Field> possibleFieldsTEN = boardService.getPossibleFields(1L, cardTEN, field1);

        assertEquals(field1.getId() + 5, possibleFieldsFIVE.get(0).getId());
        assertEquals(field1.getId() + 10, possibleFieldsTEN.get(0).getId());
        assertEquals(2, possibleFieldsTWO.size());
        assertEquals(1, possibleFieldsTWO_diffPlayer.size());
    }

    @Test
    public void getPossibleFieldsSEVEN() {
            testUser1 = new User();
            testUser1.setId(1L);
            testUser1.setEmail("test@Name.tld");
            testUser1.setUsername("testUsername");

            Player testPlayer = new Player();
            testPlayer.setUser(testUser1);

            Game testGame = new Game(testUser1, "testGame");

            Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

            Figure figure = new Figure();
            figure.setPlayer(testPlayer);

            long id = 0;
            for (Field fieldSetId : testGame.getBoard().getFields()) {
                fieldSetId.setId(id);
                id++;
            }

            Field field = testGame.getBoard().getField(1);

            field.setOccupant(figure);

            Card cardSEVEN = new NormalCard(Suit.CLUBS, Value.SEVEN);

        List<Field> possibleFieldsSEVEN = boardService.getPossibleFieldsSeven(cardSEVEN, field, 7);
        List<Field> possibleFieldsFOUR = boardService.getPossibleFieldsSeven(cardSEVEN, field, 4);

        assertEquals(7, possibleFieldsSEVEN.size());
        assertEquals(4, possibleFieldsFOUR.size());

        }
    }
