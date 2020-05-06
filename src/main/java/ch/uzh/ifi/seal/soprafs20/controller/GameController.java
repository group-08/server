package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.rest.dto.ExchangePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GameController {


    private final GameService gameService;

    GameController(GameService gameService){this.gameService = gameService;}

    @PostMapping("/game/{id}/possible")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<Field> getPossibleFields(@RequestBody MovePostDTO move, @PathVariable String id) {
        return gameService.getPossibleFields(move);
    }

    @PostMapping("/game/{id}/move")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Board move(@RequestBody MovePostDTO move, @PathVariable String id) {
        return gameService.playPlayersMove(move); //id is needed to get game
    }

    @GetMapping("/game/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getGame(@PathVariable Long id) {
        LobbyGetDTO gameNoDeck = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(gameService.getGameById(id));
        return gameNoDeck;
    }
    //get current game with id


    /** TODO: Frontend check when to exchange!
     *        show card after swap!*/
    //switching cards
    @PostMapping("/game/{id}/exchange")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public LobbyGetDTO exchangeCard(@RequestBody ExchangePostDTO exchangePostDTO,
                                    @RequestHeader("X-Token") String token, @PathVariable String id) {
        User userExchangingCard = gameService.getUserByToken(token);
        long gameId = exchangePostDTO.getId();
        Card cardToExchange = exchangePostDTO.getCard();
        LobbyGetDTO updatedGame =
                DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(gameService.letPlayersChangeCard(gameId, userExchangingCard.getId(),cardToExchange));
        return updatedGame;
    }

}
