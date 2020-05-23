package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
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

    // Return 401 if user doesn't exists or pwd and username combo is incorrect
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Error: E-Mail and Password don't match.")
    private static class IncorrectCredentials extends RuntimeException { }


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
            throw new IncorrectCredentials();
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
    }

}
