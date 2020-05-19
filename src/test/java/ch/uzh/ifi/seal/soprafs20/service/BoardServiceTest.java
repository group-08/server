package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.field.FirstField;
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


import java.util.ArrayList;
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
        testPlayer1.setId(1L);

        Player testPlayer2 = new Player();
        testPlayer2.setUser(testUser2);
        testPlayer2.setId(2L);

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

        Field field2 = testGame.getBoard().getField(2);

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
        Card cardACE= new NormalCard(Suit.CLUBS, Value.ACE);


        //apply the functions with inputs
        List<Field> possibleFieldsTWO =
                boardService.getPossibleFields(testGame, cardTWO, field0_samePlayer_as_GoalField);
        List<Field> possibleFieldsTWO_diffPlayer =
                boardService.getPossibleFields(testGame, cardTWO, field63_not_samePlayer_as_GoalField);
        List<Field> possibleFieldsFIVE = boardService.getPossibleFields(testGame, cardFIVE, field1);
        List<Field> possibleFieldsTEN = boardService.getPossibleFields(testGame, cardTEN, field1);
        List<Field> possibleFieldsACE = boardService.getPossibleFields(testGame, cardACE, field2);

        //check if the fields are correct and if multiple fields are returned
        assertEquals(field1.getId() + 5, possibleFieldsFIVE.get(0).getId());
        assertEquals(field1.getId() + 10, possibleFieldsTEN.get(0).getId());
        assertEquals(2, possibleFieldsTWO.size());
        assertEquals(2, possibleFieldsACE.size());
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

        //create Figure with Player
        Figure figure = new Figure();
        figure.setPlayer(testPlayer);

        testPlayer.addFigure(figure);

        //create new game
        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));


        //give all field id's, because they are not saved in JPA
        long id = 0;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        //get second field from board
        FirstField field = (FirstField) testGame.getBoard().getField(1);
        field.setBlocked(true);

        field.setOccupant(figure);

        GoalField goalField1 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(67);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(68);

        //set the player to fields
        goalField1.setPlayer(testPlayer);
        goalField2.setPlayer(testPlayer);
        goalField3.setPlayer(testPlayer);
        goalField4.setPlayer(testPlayer);

        //fields to be reached
        List<Field> fieldListSEVEN = new ArrayList<>();
        for (int i = 2; i<=8;i++){
            fieldListSEVEN.add(testGame.getBoard().getField(i));
        }

        List<Field> fieldListFOUR = new ArrayList<>();
        for (int i = 2; i<=5;i++){
            fieldListFOUR.add(testGame.getBoard().getField(i));
        }



        //create card seven
        Card cardSEVEN = new NormalCard(Suit.CLUBS, Value.SEVEN);
        cardSEVEN.setRemainingSteps(7);


        Card cardFOUR = new NormalCard(Suit.CLUBS, Value.SEVEN);
        cardFOUR.setRemainingSteps(4);



        List<Field> possibleFieldsSEVEN = boardService.getPossibleFieldsSeven(cardSEVEN, field);
        List<Field> possibleFieldsFOUR = boardService.getPossibleFieldsSeven(cardFOUR, field);

        //check if all fields were added to the possible fields
        assertEquals(7, possibleFieldsSEVEN.size());
        assertEquals(4, possibleFieldsFOUR.size());
        //check if the right fields were added
        assertEquals(fieldListSEVEN, possibleFieldsSEVEN);
        assertEquals(fieldListFOUR, possibleFieldsFOUR);

    }

    @Test
    public void getPossibleFieldsJokerTestBlocked(){

        //create User 1
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setEmail("test@Name.tld");
        testUser1.setUsername("testUsername");

        //create Player with User 1
        Player testPlayer = new Player();
        testPlayer.setUser(testUser1);

        //create Figure with Player
        Figure figure = new Figure();
        figure.setPlayer(testPlayer);

        testPlayer.addFigure(figure);

        //create new game
        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));


        //give all field id's, because they are not saved in JPA
        long id = 0;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        //get second field from board
        FirstField field = (FirstField) testGame.getBoard().getField(1);
        field.setPlayer(testPlayer);
        field.setBlocked(true);

        field.setOccupant(figure);

        GoalField goalField1 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(67);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(68);

        //set the player to fields
        goalField1.setPlayer(testPlayer);
        goalField2.setPlayer(testPlayer);
        goalField3.setPlayer(testPlayer);
        goalField4.setPlayer(testPlayer);


        List<Field> jokerFieldsExpectedBlocked = new ArrayList<>();
        for (int i= 2; i<=14; i++){
            jokerFieldsExpectedBlocked.add(testGame.getBoard().getField(i)); }
        jokerFieldsExpectedBlocked.add(testGame.getBoard().getField(61));


        List<Field> jokerFieldsActualBlocked = boardService.getPossibleFieldsJoker(testGame,field);

        //tests while goal fields blocked
        assertEquals(jokerFieldsExpectedBlocked.size(), jokerFieldsActualBlocked.size());
        assertTrue(jokerFieldsExpectedBlocked.containsAll(jokerFieldsActualBlocked));




    }


    @Test
    public void getPossibleFieldsJokerTestUnblocked(){

        //create User 1
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setEmail("test@Name.tld");
        testUser1.setUsername("testUsername");

        //create Player with User 1
        Player testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setUser(testUser1);

        //create Figure with Player
        Figure figure = new Figure();
        figure.setPlayer(testPlayer);

        testPlayer.addFigure(figure);

        //create new game
        Game testGame = new Game(testUser1, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));


        //give all field id's, because they are not saved in JPA
        long id = 0;
        for (Field fieldSetId : testGame.getBoard().getFields()) {
            fieldSetId.setId(id);
            id++;
        }

        //get second field from board
        FirstField field = (FirstField) testGame.getBoard().getField(1);
        field.setBlocked(true);

        field.setOccupant(figure);
        field.setPlayer(testPlayer);


        GoalField goalField1 = (GoalField) testGame.getBoard().getField(65);
        GoalField goalField2 = (GoalField) testGame.getBoard().getField(66);
        GoalField goalField3 = (GoalField) testGame.getBoard().getField(67);
        GoalField goalField4 = (GoalField) testGame.getBoard().getField(68);

        //set the player to fields
        goalField1.setPlayer(testPlayer);
        goalField2.setPlayer(testPlayer);
        goalField3.setPlayer(testPlayer);
        goalField4.setPlayer(testPlayer);


        //test for unblocked goalfields
        field.setBlocked(false);


        List<Field> jokerFieldsExpectedUnblocked = new ArrayList<>();
        for (int i= 2; i<=14; i++){
            jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(i)); }
        jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(61));
        jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(65));
        jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(66));
        jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(67));
        jokerFieldsExpectedUnblocked.add(testGame.getBoard().getField(68));

        List<Field> jokerFieldsActualUnblocked = boardService.getPossibleFieldsJoker(testGame,field);

        assertEquals(jokerFieldsExpectedUnblocked.size(),jokerFieldsActualUnblocked.size());
        assertTrue(jokerFieldsExpectedUnblocked.containsAll(jokerFieldsActualUnblocked));
    }

}


