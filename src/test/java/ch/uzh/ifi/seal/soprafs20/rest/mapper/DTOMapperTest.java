package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
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
    public void testGetUser_fromUser_toUserGetDTO_success() {
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
    public void testCreateGameGetDTO_fromGame(){
        //create Game
        User user = new User();
        user.setEmail("firstname@lastname.tld");
        user.setUsername("firstname.lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        Game game = new Game(user, "testGame");

        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(game);

        assertEquals(game.getId(), lobbyGetDTO.getId());
        assertEquals(game.getName(), lobbyGetDTO.getName());
        assertEquals(game.getGameState(), lobbyGetDTO.getGameState());
        assertEquals(game.getPlayers().get(0).getId(), lobbyGetDTO.getPlayers().get(0).getId());
    }

    @Test
    public void testCreatePlayerGetDTO_fromPlayer() {
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
