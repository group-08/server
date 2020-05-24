package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {


    private final GameService gameService;

    GameController(GameService gameService){this.gameService = gameService;}

    @PostMapping("/game/{id}/possible")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FieldGetDTO> getPossibleFields(@RequestBody MovePostDTO move, @PathVariable long id) {
        List<Field> fields = gameService.getPossibleFields(id, move);
        ArrayList<FieldGetDTO> fieldsDTO = new ArrayList<>();
        for (Field field : fields) {
            fieldsDTO.add(DTOMapper.INSTANCE.convertEntityToFieldGetDTO(field));
        }
        return fieldsDTO;
    }

    @PostMapping("/game/{id}/move")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public void move(@RequestBody MovePostDTO move, @PathVariable long id) {
        gameService.playPlayersMove(id, move); //id is needed to get game
    }

    @PostMapping("/game/{id}/move/seven")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public int moveSeven(@RequestBody MovePostDTO move, @PathVariable long id) {
        return gameService.playPlayersMoveSeven(id, move); //id is needed to get game
    }

    @GetMapping("/game/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long id, @RequestHeader("X-Token") String token) {
        return gameService.getGameById(id, token);
    }
    //get current game with id

    @PostMapping("/game/{id}/finished")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameFinishedDTO getGameFinished(@PathVariable Long id) {
        return gameService.findWinners(id);
    }



    //switching cards
    @PostMapping("/game/{id}/exchange")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody

    public GameGetDTO exchangeCard(@RequestBody ExchangePostDTO exchangePostDTO,
                                    @RequestHeader("X-Token") String token, @PathVariable long id) {
        User userExchangingCard = gameService.getUserByToken(token);
        Player playerExchangingCard = gameService.getPlayerFromUser(userExchangingCard);
        long cardId = exchangePostDTO.getCardId();
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(
                gameService.letPlayersChangeCard(
                        id,
                        playerExchangingCard.getId(),
                        cardId)
        );
    }

}
