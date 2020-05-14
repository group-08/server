package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTestWithRobosInLobby {


    @Test
    public void testLobbyFillingWithRobos(){
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setPassword("test");
        user.setToken("asdf");
        user.setStatus(UserStatus.ONLINE);

        Player testPlayer = new Player();
        testPlayer.setUser(user);

        Game game = new Game(user, "testGame");
    }

}
