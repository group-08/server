package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.board.Board;
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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<Field> getPossibleFields(@RequestBody MovePostDTO move){
        return gameService.getPossibleFields(move);
    }

    @PostMapping("/move{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Board move(@RequestBody MovePostDTO move, @PathVariable Long id) {
        return gameService.moveFigure(move, id); //id is needed to get game
    }

    @GetMapping("/game{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    //get current game with id

    @PostMapping("/game{id}/exchange")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody



}
