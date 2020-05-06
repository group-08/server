package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.user.User;
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
    public List<LobbyGetDTO> getAllLobbies(@RequestHeader("X-Token") String token){
        if(gameService.checkIfUserExists(token)) {
            List<LobbyGetDTO> allGames = gameService.getAllLobbies();
            return allGames;
        }
        else{
           return null; // throw exception?
        }
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostCreateDTO lobbyPostCreateDTO,
                                   @RequestHeader("X-Token") String token){
        String lobbyName = lobbyPostCreateDTO.getName();
        User userCreatingLobby = gameService.getUserByToken(token);
        LobbyGetDTO createdLobby = gameService.createLobby(userCreatingLobby, lobbyName);
        return createdLobby;
    }


    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyById(@PathVariable Long id){
        return gameService.getLobbyById(id);
    }


    @PostMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUser(@PathVariable Long id, @RequestHeader("X-Token") String token){
        gameService.addUser(id, token);
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
    public void startGame(@PathVariable Long id, @RequestHeader("X-Token") String token){
        if(gameService.checkToken(id, token)) {
            gameService.setUpGame(id);
        }
    }
}
