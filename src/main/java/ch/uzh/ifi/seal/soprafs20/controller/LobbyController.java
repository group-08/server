package ch.uzh.ifi.seal.soprafs20.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LobbyController {
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    //get all open games Lobbies <List<lobby>>

    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    // no return, create a lobby

    @GetMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    //return Lobby lobby


    @PostMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    @DeleteMapping("/lobby{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    @PostMapping("/lobby{id}/start")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody @PathVariable(value="id") Long id){}

}
