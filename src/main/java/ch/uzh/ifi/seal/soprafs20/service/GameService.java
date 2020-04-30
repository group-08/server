package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.User.User;
import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.entity.GameLog;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameLogRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class GameService {

    private final GameLogRepository gameLogRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameService(@Qualifier("gameLogRepository") GameLogRepository gameLogRepository,
                       @Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("userRepository") UserRepository userRepository){
        this.gameLogRepository = gameLogRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }



    public ArrayList<Field> getPossibleFields(MovePostDTO move){
        GameLog game = gameLogRepository.getOne(move.getId());
        int lastIndex = game.getBoards().size()-1;
        Board actualBoard = game.getBoards().get(lastIndex);
        Card card = move.getCard();
        Figure figure = move.getFigure();
        Field field = figure.getField();

        return actualBoard.getPossibleFields(card, field);

    }

    public Board moveFigure(MovePostDTO move) {
        GameLog game = gameLogRepository.getOne(move.getId());
        int lastIndex = game.getBoards().size()-1;
        Board actualBoard = game.getBoards().get(lastIndex);
        Figure figure = move.getFigure();
        Field targetField = move.getTargetField();
        return actualBoard.move(figure, targetField);
    }

    public void updateGame(long id, Board board, Card card){

    }

    public Game getLobbyById(long id){
        return gameRepository.findById(id).orElse(null);
    }

    public void addUser(Long id, UserPostDTO userToBeAddedDTO){
        String userToBeAddedUsername = userToBeAddedDTO.getUsername();
        User userToBeAdded = userRepository.findByUsername(userToBeAddedUsername);
        Game actualGame = gameRepository.findById(id).orElse(null);
        if(actualGame!=null) {
            actualGame.addPlayerFromUser(userToBeAdded);
        }
        //We need function to add User; Takes id and User;
    }

    /**
    public void deleteUser(Long id, UserPostDTO userToBeDeletedDTO){
        String userToBeAddedUsername = userToBeDeletedDTO.getUsername();
        User userToBeDeleted = userRepository.findByUsername(userToBeAddedUsername);
        Game actualGame = gameRepository.findById(id).orElse(null);
        if(actualGame!=null) {
            actualGame.removePlayer(userToBeDeleted);
        }
        //We need function to delete User; Takes id and User;
    }
     */


}
