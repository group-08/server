package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.User.User;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyPostCreateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LobbyController {

    private final GameService gameService;

    LobbyController(GameService gameService){this.gameService = gameService;}


    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    //get all open games Lobbies <List<Game>>

    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createLobby(@RequestBody LobbyPostCreateDTO lobbyPostCreateDTO){
        lobbyPostCreateDTO.getName();
        // I need a user!
        // I need a function create Lobby or something!
    }
    // no return, create a lobby

    @GetMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game getLobbyById(@PathVariable Long id){
        return gameService.getLobbyById(id);
    }


    @PostMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUser(@PathVariable Long id, @RequestBody UserPostDTO userPostDTO){
        gameService.addUser(id, userPostDTO);
    }
                        //add a user to a lobby

    @DeleteMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void deleteUser(@PathVariable Long id, @RequestBody UserPostDTO userPostDTO){
        gameService.
    }

                        @PostMapping("/lobby{id}/start")
                        @ResponseStatus(HttpStatus.OK)
    public @ResponseBody @PathVariable(value="id") Long id){}

}
