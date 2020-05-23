package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    private final GameService gameService;

    LobbyController(GameService gameService){this.gameService = gameService;}

    // Return 404 if lobby not found
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Error: Lobby doesn't exist")
    private static class LobbyNotFound extends RuntimeException { }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Error: User doesn't exist")
    private static class UserNotFound extends RuntimeException { }


    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies(@RequestHeader("X-Token") String token){
        if(gameService.checkIfUserExists(token)) {
            return gameService.getAllLobbies();
        }
        else{
           throw new UserNotFound();
        }
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostCreateDTO lobbyPostCreateDTO,
                                   @RequestHeader("X-Token") String token){
        String lobbyName = lobbyPostCreateDTO.getName();
        User userCreatingLobby = gameService.getUserByToken(token);
        return gameService.createLobby(userCreatingLobby, lobbyName);
    }


    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobbyById(@PathVariable Long id){
        Game lobby = gameService.getLobbyById(id);
        if (lobby == null) {
            throw new LobbyNotFound();
        }
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }


    @PostMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUser(@PathVariable Long id, @RequestHeader("X-Token") String token){
        gameService.addUser(id, token);
    }

    @DeleteMapping("/lobby/{id}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteUser(@PathVariable Long id, @RequestHeader("X-Token") String token, @PathVariable Long userId){
        if (gameService.checkToken(id, token)) {
            gameService.removeUser(id, userId);
        } else {
            gameService.removeSelf(id, token, userId);
        }
    }

    @PostMapping("/lobby/{id}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable Long id, @RequestHeader("X-Token") String token){
        if(gameService.checkToken(id, token)) {
            gameService.setUpGame(id);
        }
    }

    @DeleteMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteLobby(@PathVariable Long id, @RequestHeader("X-Token") String token) {
        if(gameService.checkToken(id, token)) {
            gameService.deleteGame(id);
        }
    }
}
