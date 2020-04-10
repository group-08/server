package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GameController {


    private final GameService gameService;

    GameController(GameService gameService){this.gameService = gameService;}

    @PostMapping("/game")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Game playMove(@RequestBody MovePostDTO move){
        ArrayList<Field> possibleMoves = gameService.getPossibleFields(move);
        return null;
    }


}
