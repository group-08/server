package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    private final GameService gameService;

    LobbyController(GameService gameService){this.gameService = gameService;}


    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getAllLobbies(){
        // TODO require token
        return gameService.getAllLobbies();
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createLobby(@RequestBody LobbyPostCreateDTO lobbyPostCreateDTO,
                            @RequestHeader("X-Token") String token){
        String lobbyName = lobbyPostCreateDTO.getName();
        User userCreatingLobby = gameService.getUserByToken(token);
        gameService.createLobby(userCreatingLobby, lobbyName);
    }


    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game getLobbyById(@PathVariable Long id){
        return gameService.getLobbyById(id);
    }


    @PostMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUser(@PathVariable Long id, @RequestBody UserPostDTO userPostDTO){
        // TODO require token (which can be used to add the user since users can only add themesleves
        gameService.addUser(id, userPostDTO);
    }

    @DeleteMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void deleteUser(@PathVariable Long id, @RequestBody UserPostDTO userPostDTO){
        // TODO require token
        // TODO not userPostDTO object but probably just a string or something
        // TODO be made later
    }

    @PostMapping("/lobby/{id}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable Long id){
        // TODO require token and check gamemaster can only start game
        gameService.setUpGame(id);
    }

}
