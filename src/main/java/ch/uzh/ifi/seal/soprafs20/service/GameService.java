package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.entity.GameLog;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public boolean checkIfMoveIsValid(MovePostDTO move){

        GameLog game = gameRepository.getOne(move.getId());
        int lastIndex = game.getBoards().size()-1;
        Board actualBoard = game.getBoards().get(lastIndex);
        /**
        Field currentField = Figure.getFieldId();
         targetField = getTargetField(currentfield, move.getCard)
         Boolean isLegal = checkIfLegalMove(currentField, targetField)


        */
        return false;
    }

    public Field getTargetField(Field currentField, Card card){
        return Field;
    }

    public boolean checkIfLegalMove(Field currentField, Field targetField){
        return true;
    }

    public void updateGame(long id, Board board, Card card){

    }


}
