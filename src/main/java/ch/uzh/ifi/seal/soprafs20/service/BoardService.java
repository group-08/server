package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.field.FirstField;
import ch.uzh.ifi.seal.soprafs20.field.GoalField;
import ch.uzh.ifi.seal.soprafs20.field.HomeField;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BoardService {

    private final GameRepository gameRepository;

    @Autowired
    public BoardService(@Qualifier("gameRepository") GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public Board getBoard(long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        assert game != null;
        return game.getBoard();
    }

    /**
     * Sends the figure home
     * @param gameId ID of game
     * @param figure that has to be sent home
     */
    public void sendFigureHome(long gameId, Figure figure) {
        Board board = this.getBoard(gameId);
        Player playerOfFigure = figure.getPlayer();
        for(Field field : board.getFields()){
            if(field instanceof HomeField){
                if(((HomeField) field).getPlayer()==playerOfFigure && field.getOccupant() == null){
                    field.setOccupant(figure);
                    return;
                }
            }
        }
    }

    public void saveAndFlushBoard(long gameId)  {
        Board board = this.getBoard(gameId);
        BoardRepository repository = board.getBoardRepository();
        repository.saveAndFlush(board);
    }

    public Board move(long gameId, Figure figure, Field targetField) {
        Board board = this.getBoard(gameId);
        Field currentField = board.getCurrentField(figure);
        if (targetField.getOccupant() != null) {
            Figure occupant = targetField.getOccupant();
            this.sendFigureHome(gameId, occupant);
            currentField.setOccupant(null);
            targetField.setOccupant(figure);
        }
        else {
            currentField.setOccupant(null);
            targetField.setOccupant(figure);
        }
        if (targetField instanceof FirstField && currentField instanceof HomeField) {
            ((FirstField) targetField).setBlocked(true);
        }
        if (currentField instanceof FirstField && !(targetField instanceof FirstField)) {
            ((FirstField) currentField).setBlocked(false);
        }

        this.saveAndFlushBoard(gameId);

        return board;
    }

    public boolean checkIfAllTargetFieldsOccupied(long gameId, Player player) {
        Board board = this.getBoard(gameId);
        List<Field> fieldsOfBoard = board.getFields();
        int count = 0;
        for (Field field : fieldsOfBoard) {
            if (field instanceof GoalField && ((GoalField) field).getPlayer() == player && field.getOccupant() != null) {
                count++;
            }
        }
        return count == 4;
    }



}
