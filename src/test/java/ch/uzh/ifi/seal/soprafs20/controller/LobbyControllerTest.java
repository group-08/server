package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Collections;
import java.util.List;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private GameService gameService;


    @Test
    public void givenLobbies_whenGetLobbies_thenReturnJsonArray() throws Exception {
        // given

        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.OFFLINE);


        Game lobby = new Game(user, "testGame");

        GameGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(lobby);

        List<GameGetDTO> allGames = Collections.singletonList(lobbyGetDTO);

        given(gameService.getAllLobbies()).willReturn(allGames);
        given(gameService.checkIfUserExists(Mockito.anyString())).willReturn(true);


        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON)
                .header("X-Token", "1");


        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(lobby.getName())))
                .andExpect(jsonPath("$[0].gameState", is(lobby.getGameState().toString())))
                .andExpect(jsonPath("$[0].players", hasSize(1)));
    }

    @Test
    public void createLobby_whenPostLobbies() throws Exception{
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.OFFLINE);

        LobbyPostCreateDTO lobbyPostCreateDTO = new LobbyPostCreateDTO();
        lobbyPostCreateDTO.setName("Allo");

        GameGetDTO gameGetDTO = new GameGetDTO();
        gameGetDTO.setName(lobbyPostCreateDTO.getName());

        given(gameService.getUserByToken(Mockito.anyString())).willReturn(user);
        given(gameService.createLobby(Mockito.any(), Mockito.anyString())).willReturn(gameGetDTO);

        MockHttpServletRequestBuilder postRequest = post("/lobbies").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostCreateDTO))
                .header("X-Token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void getLobbyById_whenGetLobby() throws Exception{
        User user = new User();
        user.setUsername("firstname.lastname");
        user.setEmail("firstname@lastname.ch");
        user.setStatus(UserStatus.OFFLINE);

        Game lobby = new Game(user, "testGame");
        lobby.setId((long)1);
        lobby.setWeatherState(WeatherState.CASUAL);
        

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(lobby);

        given(gameService.getLobbyById(1)).willReturn(DTOMapper.INSTANCE.convertEntityToGameGetDTO(lobby));

        MockHttpServletRequestBuilder getRequest = get("/lobby/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.gameState", is(lobby.getGameState().toString())));

    }

    @Test
    public void addUserToLobby() throws Exception{


        Mockito.doNothing().when(gameService).addUser(Mockito.any(), Mockito.anyString());

        MockHttpServletRequestBuilder postRequest = post("/lobby/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

    }

    @Test
    public void startGame() throws Exception{

        given(gameService.checkToken(Mockito.any(), Mockito.anyString())).willReturn(true);
        Mockito.doNothing().when(gameService).setUpGame(Mockito.anyLong());

        MockHttpServletRequestBuilder postRequest = post("/lobby/{id}/start", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Token", "1");


        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
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
