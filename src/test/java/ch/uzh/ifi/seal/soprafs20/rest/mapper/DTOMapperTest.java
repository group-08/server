package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.user.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
class DTOMapperTest {
    @Test
    void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("user@name.tld");
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getEmail(), user.getEmail());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertTrue(user.checkPassword(userPostDTO.getPassword()));
    }

    @Test
    void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setEmail("firstname@lastname.tld");
        user.setUsername("firstname.lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getEmail(), userGetDTO.getEmail());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    void testCreateGameGetDTO_fromGame(){
        //create Game
        User user = new User();
        user.setEmail("firstname@lastname.tld");
        user.setUsername("firstname.lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        Game game = new Game(user, "testGame");
        game.setGameState(GameState.RUNNING);

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(game);

        assertEquals(game.getId(), lobbyGetDTO.getId());
        assertEquals(game.getName(), lobbyGetDTO.getName());
        assertEquals(game.getGameState(), lobbyGetDTO.getGameState());
        assertEquals(game.getPlayers().get(0).getId(), lobbyGetDTO.getPlayers().get(0).getId());
    }

    @Test
    void testCreatePlayerGetDTO_fromPlayer() {
        Player testPlayer = new Player();
        testPlayer.setColour(Colour.GREEN);

        User testUser = new User();
        testUser.setId(1L);

        testPlayer.setUser(testUser);

        PlayerGetDTO testPlayerDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(testPlayer);
        assertEquals(testPlayer.getColour(), testPlayerDTO.getColour());
        assertEquals(testPlayer.getUser().getId(), testPlayerDTO.getUser().getId());
    }
}
