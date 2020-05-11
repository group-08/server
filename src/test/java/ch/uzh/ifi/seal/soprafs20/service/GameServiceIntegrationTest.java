package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.cards.*;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.user.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Qualifier("cardRepository")
    @Autowired
    private CardRepository cardRepository;

    @Qualifier("figureRepository")
    @Autowired
    private FigureRepository figureRepository;

    @Qualifier("fieldRepository")
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private GameService gameService;

    private Game game;

    private long ID;

    @BeforeEach
    public void init() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
        cardRepository.deleteAll();

        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        this.game = new Game(user, "testGame");

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
        this.ID = game.getId();

    }

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Test
    public void SetUpGameIntegrationTest() {
        // Check if every player has 4 figures
        assertEquals(4, game.getPlayer(0).getFigures().size());
        assertEquals(4, game.getPlayer(1).getFigures().size());
        assertEquals(4, game.getPlayer(2).getFigures().size());
        assertEquals(4, game.getPlayer(3).getFigures().size());

        // Check if Deck has 30 cards after distribution
        assertEquals(30, game.getDeck().getCardsInDeck().size());

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

    @RepeatedTest(value = 50)
    public void MoveFigureTest() {
        ////////// CARD SETUP ///////////
        Card KingClubs = new NormalCard(Suit.CLUBS, Value.KING);
        cardRepository.saveAndFlush(KingClubs);

        Player player = game.getPlayer(0);


        /////////// MOVE SETUP ////////////
        // MovePostDTO setup
        MovePostDTO move = new MovePostDTO();
        move.setCardId(KingClubs.getId());
        move.setFigureId(player.getFigures().get(0).getId());
        move.setTargetFieldId(game.getBoard().getField(1).getId());


        /////////// MAKE MOVE //////////////
        gameService.playPlayersMove(game.getId(), move);
        Game gameAfterMove = gameRepository.findById(ID).orElse(null);
        assert gameAfterMove != null;


        ////////// TEST MOVE ////////////
        // Setup Tests
        Field homeField = gameAfterMove.getBoard().getField(81);
        Field targetField = gameAfterMove.getBoard().getField(1);

        long playerId = player.getId();
        Player playerNew = playerRepository.findById(playerId).orElse(null);
        assert playerNew != null;
        Field fieldReadOutOfFigure = playerNew.getFigures().get(0).getField();
        Figure figureofPlayer = playerNew.getFigures().get(0);

        // Here follow assertions if move executed correctly
        // Created a card to move figure, but player might have not that card so it wouldn't remove it from his hands
        // therefore after move maybe still has 6 cards
        assertNotNull(targetField.getOccupant());
        Assertions.assertEquals(targetField.getId(), fieldReadOutOfFigure.getId());
        Assertions.assertEquals(targetField.getOccupant().getId(), figureofPlayer.getId());
    }

    @RepeatedTest(value = 1)
    public void PlayRounds() {
        /////////// MOVE LOGIC ///////////
        List<Card> playedCards = new ArrayList<>();
        for (int i = 0; i < 40; i++)   {
            System.out.println(i+1);
            game = gameRepository.findById(ID).orElse(null);
            assert game!=null;
            Player player = game.getPlayers().get(0);
            List<Card> playerHand = new ArrayList<>(player.getHand());
            for (Card card : playerHand) {
                if (card instanceof JokerCard) {
                    player.getHand().remove(card);
                    Card newCard = new NormalCard(Suit.SPADES, Value.ACE);
                    cardRepository.saveAndFlush(newCard);
                    player.getHand().add(newCard);
                }
            }
            MovePostDTO move = gameService.automaticMove(player, ID);
            if (move == null) {
                player.getHand().remove(0);
                player.getHand().add(new NormalCard(Suit.SPADES, Value.ACE));
                move = gameService.automaticMove(player, ID);
            }
            playerRepository.saveAndFlush(player);
            long cardId = move.getCardId();
            Card card = gameService.getCardFromId(cardId);
            long playerId = player.getId();
            if (card.getValue() == Value.SEVEN) {
                while (move.getRemainingSeven() > 0) {
                    int remaining = gameService.playPlayersMoveSeven(ID, move);
                    player = playerRepository.findById(playerId).orElse(null);
                    assert player != null;
                    move = gameService.automaticMoveSeven(ID, card, player, remaining);
                }
                playedCards.add(card);
            } else {
                playedCards.add(card);
                gameService.playPlayersMove(game.getId(), move);
            }
        }
    }

    @Test
    public void sendHome() {
        long gameId = game.getId();
        Figure figure1 = game.getPlayer(0).getFigures().get(0);
        long figure1Id = figure1.getId();
        Field field10 = game.getBoard().getField(10);
        long field10Id = field10.getId();
        figure1.getField().setOccupant(null);
        field10.setOccupant(figure1);
        figure1.setField(field10);

        Figure figure2 = game.getPlayer(1).getFigures().get(0);
        long figure2Id = figure2.getId();
        Field field15 = game.getBoard().getField(15);
        long field15Id = field15.getId();
        Field homeField2 = figure2.getField();
        long homeField2Id = homeField2.getId();
        figure2.getField().setOccupant(null);
        field15.setOccupant(figure2);
        figure2.setField(field15);

        Card card = new NormalCard(Suit.HEARTS, Value.FIVE);
        cardRepository.saveAndFlush(card);

        MovePostDTO killerMove = new MovePostDTO();
        killerMove.setFigureId(figure1.getId());
        killerMove.setCardId(card.getId());
        killerMove.setTargetFieldId(field15.getId());

        gameRepository.saveAndFlush(game);
        gameService.playPlayersMove(gameId, killerMove);
        Field field10new = fieldRepository.findById(field10Id).orElse(null);
        assert field10new != null;
        Field field15new = fieldRepository.findById(field15Id).orElse(null);
        assert field15new != null;
        Figure figure2new = figureRepository.findById(figure2Id).orElse(null);
        assert figure2new != null;
        Field homeField2new = fieldRepository.findById(homeField2Id).orElse(null);
        assert homeField2new != null;
        Figure figure1new = figureRepository.findById(figure1Id).orElse(null);
        assert figure1new != null;

        assertNull(field10new.getOccupant());
        
        assertEquals(figure1.getId(), field15new.getOccupant().getId());
        assertEquals(field15new.getId(), figure1new.getField().getId());

        assertEquals(homeField2.getId(), figure2new.getField().getId());
        assertEquals(figure2.getId(), homeField2new.getOccupant().getId());
    }

}
