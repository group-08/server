package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.*;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        @Qualifier("gameRepository") GameRepository gameRepository){
        this.boardRepository = boardRepository;
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

    public Board move(long gameId, Figure figure, Field targetField) {
        Board board = this.getBoard(gameId);
        Field currentField = this.getCurrentField(figure);
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

        this.boardRepository.saveAndFlush(board);

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

    /**
     * Takes a Figure and gets Figures current Field
     * @param figure figure you want the current field of
     * @return the current field of the figure
     */
    public Field getCurrentField(Figure figure) {
        return figure.getField();
    }

    private ArrayList<Integer> getMoveValues(Card card){
        ArrayList<Integer> possibleValues = new ArrayList<>();
        if(card.getValue()== Value.ACE){
            possibleValues.add(1);
            possibleValues.add(11);
        }
        else{
            possibleValues.add(card.getValue().getValue());
        }

        return possibleValues;
    }

    /**
     * Get the possible field so we can either automatically move the piece or display all possible fields
     * @param card the card that was played
     * @param field field the card is being palyed on
     * @return List of all possible fields the player on field could land on
     */
    public ArrayList<Field> getPossibleFields(long gameId, Card card, Field field) {
        Game actualGame = gameRepository.findById(gameId).orElse(null);
        assert actualGame != null;
        Board board = actualGame.getBoard();
        ArrayList<Integer> moveValues = getMoveValues(card);

        if(field instanceof HomeField && (card.getValue() == Value.KING || card.getValue() == Value.ACE)){
            Player playerOfField = ((HomeField) field).getPlayer();
            for (Field key : board.getFields()){
                if(key instanceof FirstField && ((FirstField) key).getPlayer()==playerOfField){
                    ArrayList<Field> fields = new ArrayList<>();
                    fields.add(key);
                    return fields;
                }
            }
        }
        if (card.getValue() == Value.FOUR) {
            ArrayList<Field> forwardFour = getFieldsBoard(field, moveValues);
            ArrayList<Field> backwardFour = getPossibleFieldsFour(card, field, board);
            forwardFour.addAll(backwardFour);
            return forwardFour;
        }
        return this.getFieldsBoard(field, moveValues);
    }

    public ArrayList<Field> getPossibleFieldsFour(Card card, Field field, Board gameBoard){
        int level = 0;
        List<Field> fields = gameBoard.getFields();
        ArrayList<Field> possibleFields = new ArrayList<>();
        int moveValue = card.getValue().getValue();
        Player player = field.getOccupant().getPlayer();
        Queue<Field> queue = new LinkedList<>();
        queue.add(field);
        queue.add(null);
        if (field instanceof GoalField){
            return possibleFields;
        }
        while(!(queue.isEmpty()) && level < moveValue) {
            Field temp = queue.poll();
            if (temp==null){
                level++;
                queue.add(null);
            }
            else {
                for(Field boardField : fields){
                    if(boardField.getAdjacencyList().contains(temp)) {
                        if(boardField instanceof HomeField){
                            assert true;
                        }
                        else if (boardField instanceof FirstField) {
                            if (((FirstField) boardField).getBlocked()) {
                                assert true;
                            }
                            else {
                                queue.add(boardField);
                            }
                        }
                        else{
                            queue.add(boardField);
                        }
                    }
                    if(temp instanceof FirstField){
                        for(Field pField : temp.getAdjacencyList()){
                            if(pField instanceof GoalField){
                                if(((GoalField) pField).getPlayer()==player){
                                    queue.add(pField);
                                }
                            }
                        }
                    }
                }
            }
        }
        while (!queue.isEmpty()) {
            if (queue.peek() != null) {
                possibleFields.add(queue.poll());
            }
            else {
                queue.poll();
            }
        }
        return possibleFields;
    }

    public ArrayList<Field> getPossibleFieldsSeven(Card card, Field field, int value){
        ArrayList<Field> possibleFields = new ArrayList<>();
        for(int i=1; i<=value;i++) {
            ArrayList<Integer> values = new ArrayList<>();
            values.add(value);
            possibleFields.addAll(this.getFieldsBoard(field, values));
        }
        return possibleFields;
    }

    public ArrayList<Field> getFieldsBoard(Field fieldToCheck, ArrayList<Integer> valuesToCheck){
        int level = 0;
        ArrayList<Field> possibleFields = new ArrayList<>();
        Queue<Field> queue = new LinkedList<>();
        queue.add(fieldToCheck);
        queue.add(null);
        for(int moveValue : valuesToCheck) {
            while (!queue.isEmpty() && level < moveValue) {
                Field temp = queue.poll();
                if (temp == null) {
                    level++;
                    queue.add(null);
                }
                else {
                    List<Field> adjFields = temp.getAdjacencyList();
                    if (temp instanceof FirstField && ((FirstField) temp).getBlocked()) {
                        for (Field field1 : adjFields) {
                            if (field1 instanceof GoalField) {
                                adjFields.remove(field1);
                            }
                        }
                    }
                    for (Field f : adjFields) {
                        if (f instanceof FirstField && ((FirstField) f).getBlocked()) {
                            assert true;
                        }
                        else if (f instanceof GoalField && ((GoalField) f).getPlayer() != fieldToCheck.getOccupant().getPlayer()) {
                            assert true;
                        }
                        else {
                            queue.add(f);
                        }
                    }
                }
            }
            while (!queue.isEmpty()) {
                if (queue.peek() != null) {
                    possibleFields.add(queue.poll());
                }
                else {
                    queue.poll();
                }
            }
        }
        return possibleFields;
    }

    public ArrayList<Field> getPossibleFieldsJack(long gameId, Card card, Field field){
        ArrayList<Field> possibleFields = new ArrayList<>();
        Player playerOnField = field.getOccupant().getPlayer();
        Game actualGame = gameRepository.findById(gameId).orElse(null);
        assert actualGame != null;
        Board board = actualGame.getBoard();

        for(Field iterField : board.getFields()){
            if(iterField.getOccupant()!=null){
                if(iterField.getOccupant().getPlayer()!=playerOnField && (iterField instanceof CasualField
                        || (iterField instanceof FirstField && !(((FirstField) iterField).getBlocked())))){
                    possibleFields.add(iterField);
                }
            }
        }
        return possibleFields;
    }

}







