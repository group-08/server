package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.CasualField;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void getPossibleFields_Test () throws Exception{

        MovePostDTO move = new MovePostDTO();
        Figure figure = new Figure();
        figure.setId(1L);

        move.setFigure(figure);
        move.setId(1L);

        Field field = new CasualField();
        Field field2 = new CasualField();

        field.setId(2);
        field.setId(3);

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(field);
        fields.add(field2);

        given(gameService.getPossibleFields(Mockito.any())).willReturn(fields);


        MockHttpServletRequestBuilder postRequest = post("/game").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(move))
                .header("X-Token", "1");

        mockMvc.perform(postRequest)
                .andExpect(jsonPath("$[0].id", is((int)field.getId())))
                .andExpect(jsonPath("$[1].id", is((int)field2.getId())));
    }

    @Test
    public void getGameById_Test() throws Exception{
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.OFFLINE);

        Game game = new Game(user, "testGame");
        game.setId((long)1);
        game.setWeatherState(WeatherState.CASUAL);
        game.setBoard(null);

        given(gameService.getGameById(Mockito.anyLong())).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/game/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameState", is(game.getGameState().toString())))
                .andExpect(jsonPath("$.id", is(game.getId().intValue())));

    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}