package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ExchangePostDTO;
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

    @PostMapping("/move")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Board move(@RequestBody MovePostDTO move) {
        return gameService.moveFigure(move); //id is needed to get game
    }

    @GetMapping("/game/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game getGame(@PathVariable Long id) {
        return gameService.getGameById(id);
    }
    //get current game with id

    @PostMapping("/game/{id}/exchange")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game exchangeCard(@RequestBody ExchangePostDTO exchangePostDTO) {
        return null;
    }

}
