package ch.uzh.ifi.seal.soprafs20.api;


import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.WeatherService;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@SpringBootTest
class ApiTest {


    @Autowired
    private GameService gameService;

    @Autowired
    private WeatherService weatherService;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void ApiTestFull() {
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

        List<WeatherState> weatherStates = new ArrayList<>();
        weatherStates.add(WeatherState.RAINY);
        weatherStates.add(WeatherState.SUNNY);
        weatherStates.add(WeatherState.WINDY);
        long gameID = game.getId();

        gameService.setUpGame(gameID);
        weatherService.updateWeather(game);
        gameRepository.saveAndFlush(game);

        game = gameRepository.findById(gameID).orElse(null);
        assert game!=null;
        assert game.getWeatherState() != null;

        Assertions.assertTrue(weatherStates.contains(game.getWeatherState()));
        Assertions.assertNotEquals(WeatherState.UNKNOWN, game.getWeatherState());
    }


}
