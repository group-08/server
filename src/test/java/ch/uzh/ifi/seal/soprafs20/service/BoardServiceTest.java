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
        // create 2 users
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setEmail("test@Name.tld");
        testUser1.setUsername("testUsername");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setEmail("test@Name");
        testUser2.setUsername("test2");

        // create 2 players
        Player testPlayer1 = new Player();
        testPlayer1.setUser(testUser1);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(testUser2);

        //create game
        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

        //create figures with different players
        Figure figure1 = new Figure();
        figure1.setPlayer(testPlayer1);

        Figure figure2 = new Figure();
        figure2.setPlayer(testPlayer2);

        //give fields ids
        long id = 0;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        //create fields with figures that belong to different players
        Field field1 = testGame.getBoard().getField(1);
        field1.setOccupant(figure1);

        Field field0_samePlayer_as_GoalField = testGame.getBoard().getField(1);
        field0_samePlayer_as_GoalField.setOccupant(figure1);

        Field field63_not_samePlayer_as_GoalField = testGame.getBoard().getField(64);
        field63_not_samePlayer_as_GoalField.setOccupant(figure2);

        // get all the goalFields of first player
        GoalField goalField1 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(67);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(68);

        //set the player 1 to fields
        goalField1.setPlayer(testPlayer1);
        goalField2.setPlayer(testPlayer1);
        goalField3.setPlayer(testPlayer1);
        goalField4.setPlayer(testPlayer1);

        //create cards
        Card cardFIVE = new NormalCard(Suit.CLUBS, Value.FIVE);
        Card cardTEN = new NormalCard(Suit.CLUBS, Value.TEN);
        Card cardTWO = new NormalCard(Suit.CLUBS, Value.TWO);


        //apply the functions with inputs
        List<Field> possibleFieldsTWO =
                boardService.getPossibleFields(testGame, cardTWO, field0_samePlayer_as_GoalField);
        List<Field> possibleFieldsTWO_diffPlayer =
                boardService.getPossibleFields(testGame, cardTWO, field63_not_samePlayer_as_GoalField);
        List<Field> possibleFieldsFIVE = boardService.getPossibleFields(testGame, cardFIVE, field1);
        List<Field> possibleFieldsTEN = boardService.getPossibleFields(testGame, cardTEN, field1);

        //check if the fields are correct and if multiple fields are returned
        assertEquals(field1.getId() + 5, possibleFieldsFIVE.get(0).getId());
        assertEquals(field1.getId() + 10, possibleFieldsTEN.get(0).getId());
        assertEquals(2, possibleFieldsTWO.size());
        assertEquals(1, possibleFieldsTWO_diffPlayer.size());

    }

    @Test
    public void getPossibleFieldsFOUR(){
        // create 2 users
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setEmail("test@Name.tld");
        testUser1.setUsername("testUsername");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setEmail("test@Name");
        testUser2.setUsername("test2");

        // create 2 players
        Player testPlayer1 = new Player();
        testPlayer1.setUser(testUser1);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(testUser2);

        //create game
        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

        //create figures with different players
        Figure figure1 = new Figure();
        figure1.setPlayer(testPlayer1);

        Figure figure2 = new Figure();
        figure2.setPlayer(testPlayer2);

        //give fields ids
        long id = 1;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        //create fields with figures that belong to different players
        Field field2 = testGame.getBoard().getField(2);
        field2.setOccupant(figure1);

        Field field1_samePlayer_as_GoalField = testGame.getBoard().getField(1);
        field1_samePlayer_as_GoalField.setOccupant(figure1);

        Field field63_not_samePlayer_as_GoalField = testGame.getBoard().getField(64);
        field63_not_samePlayer_as_GoalField.setOccupant(figure2);

        // get all the goalFields of first player
        GoalField goalField1 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(67);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(68);

        //set the player 1 to fields
        goalField1.setPlayer(testPlayer1);
        goalField2.setPlayer(testPlayer1);
        goalField3.setPlayer(testPlayer1);
        goalField4.setPlayer(testPlayer1);

        Card cardFOUR = new NormalCard(Suit.CLUBS, Value.FOUR);

        List<Field> possibleFieldsFOUR = boardService.getPossibleFields(testGame, cardFOUR, field1_samePlayer_as_GoalField);

        Field field61 = testGame.getBoard().getField(61);
        Field field68 = testGame.getBoard().getField(68);
        Field field5 = testGame.getBoard().getField(5);

        assertEquals(3, possibleFieldsFOUR.size());
        assertTrue(possibleFieldsFOUR.contains(field61));
        assertTrue(possibleFieldsFOUR.contains(field68));
        assertTrue(possibleFieldsFOUR.contains(field5));
    }

    @Test
    public void getPossibleFieldsSEVEN() {

            //create User 1
            testUser1 = new User();
            testUser1.setId(1L);
            testUser1.setEmail("test@Name.tld");
            testUser1.setUsername("testUsername");

            //create Player with User 1
            Player testPlayer = new Player();
            testPlayer.setUser(testUser1);

            //create new game
            Game testGame = new Game(testUser1, "testGame");

            Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

            //create Figure with Player
            Figure figure = new Figure();
            figure.setPlayer(testPlayer);

            //give all field id's, because they are not saved in JPA
            long id = 0;
            for (Field fieldSetId : testGame.getBoard().getFields()) {
                fieldSetId.setId(id);
                id++;
            }

            //get second field from board
            Field field = testGame.getBoard().getField(1);

            field.setOccupant(figure);

            //create card seven
            //Card cardSEVEN = new NormalCard(Suit.CLUBS, Value.SEVEN);

        //List<Field> possibleFieldsSEVEN = boardService.getPossibleFieldsSeven(cardSEVEN, field, 7);
        //List<Field> possibleFieldsFOUR = boardService.getPossibleFieldsSeven(cardSEVEN, field, 4);

        //check if all fields were added to the possible fields
        //assertEquals(7, possibleFieldsSEVEN.size());
        //assertEquals(4, possibleFieldsFOUR.size());

        }
    }
