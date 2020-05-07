package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        List<LobbyGetDTO> allGames = Collections.singletonList(lobbyGetDTO);

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

        Player player = new Player();
        player.setUser(user);

        LobbyGetDTO lobbyGetDTO = new LobbyGetDTO();
        lobbyGetDTO.setId(1L);
        lobbyGetDTO.setName(lobbyPostCreateDTO.getName());
        lobbyGetDTO.setHost(user);
        lobbyGetDTO.setGameState(GameState.PENDING);
        lobbyGetDTO.setPlayers(Collections.singletonList(player));

        given(gameService.getUserByToken(Mockito.anyString())).willReturn(user);
        given(gameService.createLobby(Mockito.any(), Mockito.anyString())).willReturn(lobbyGetDTO);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
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

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        given(gameService.getLobbyById(1)).willReturn(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));

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
