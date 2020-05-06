package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostLoginDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.AuthService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User login(@RequestBody UserPostLoginDTO userPostLoginDTO) throws Exception {
        // Login the user
        User loginUser = null;
        try {
            loginUser = authService.login(userPostLoginDTO);
        }
        catch (IllegalAccessException e) {
            // Wrong password
            // TODO return proper 403
            throw new Exception("Wrong password");
        }
        // convert internal representation of user back to API
        return loginUser;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userInput.setPassword(userPostDTO.getPassword());

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        //should return user
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logout(@RequestHeader String token) {
        authService.logout(token);
        // TODO require token from user
        // TODO remove (invalidate) token
        // TODO Set status to offline
    }

}
