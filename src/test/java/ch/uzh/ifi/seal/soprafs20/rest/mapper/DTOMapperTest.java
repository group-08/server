package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.user.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
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

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        assertEquals(game.getId(), gameGetDTO.getId());
        assertEquals(game.getName(), gameGetDTO.getName());
        assertEquals(game.getGameState(), gameGetDTO.getGameState());
        assertEquals(game.getPlayers(), gameGetDTO.getPlayers());
    }
}
