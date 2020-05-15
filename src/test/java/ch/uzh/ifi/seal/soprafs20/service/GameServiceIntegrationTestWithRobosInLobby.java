package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTestWithRobosInLobby {

    @Autowired
    private GameService gameService;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testLobbyFillingWithRobos(){
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);
        userRepository.saveAndFlush(user);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");
        gameRepository.saveAndFlush(game);


        long gameID = game.getId();

        gameService.setUpGame(gameID);

        game = gameRepository.findById(gameID).orElse(null);
        assert game!=null;


        Assertions.assertEquals(4,game.getPlayers().size());
        for(int i=0; i<3; i++){
            for(int q=i+1; q<4; q++){
                Player player1 = game.getPlayer(i);
                Player player2 = game.getPlayer(q);
                Assertions.assertNotEquals(player1, player2);
            }
        }
    }

}
