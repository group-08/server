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

    public Board getBoard(Game game) {
        return game.getBoard();
    }

    /**
     * Sends the figure home
     * @param game game
     * @param figure that has to be sent home
     */
    public void sendFigureHome(Game game, Figure figure) {
        Board board = this.getBoard(game);
        Player playerOfFigure = figure.getPlayer();
        Field currentField = this.getFieldfromFigure(game, figure);
        Figure currentFigure = currentField.getOccupant();
        for(Field field : board.getFields()){
            if(field instanceof HomeField){
                if(((HomeField) field).getPlayer()==playerOfFigure && field.getOccupant() == null){
                    currentField.setOccupant(null);
                    field.setOccupant(currentFigure);
                    currentFigure.setField(field);
                    return;
                }
            }
        }
    }


    public Field matchFields(Game game, Field currentField) {
        for (Field field : this.getBoard(game).getFields()) {
            if (field.getId() == currentField.getId()) {
                return field;
            }
        }
        return null;
    }

    public Field getFieldfromFigure(Game game, Figure figure) {
        for (Field field : this.getBoard(game).getFields()) {
            if (field.getId() == figure.getField().getId()) {
                return field;
            }
        }
        return null;
    }

    public void swapJack(Game game, Figure figure, Field targetFieldObject) {
        Field targetField = this.matchFields(game, targetFieldObject);
        assert targetField.getOccupant() != null;
        Field currentField = getFieldfromFigure(game, figure);
        Figure currentFigure = currentField.getOccupant();
        Figure swapFigure = targetField.getOccupant();

        currentFigure.setField(targetField);
        targetField.setOccupant(currentFigure);
        swapFigure.setField(currentField);
        currentField.setOccupant(swapFigure);
    }

    public int moveSeven(Game game, Figure figure, Field targetFieldObject, int remaining) {
        // move of seven

        Field targetField = this.matchFields(game, targetFieldObject);
        Field currentField = getFieldfromFigure(game, figure);
        Field actualField = currentField;
        Figure occ = currentField.getOccupant();
        assert occ != null;

        List<Field> fieldsToMove = new ArrayList<>();
        // get all fields in between current and target field
        while (actualField.getId() != targetField.getId()) {
            if (actualField.getAdjacencyList().size() > 1) {
                if (targetField instanceof GoalField)   {
                    for (Field field : actualField.getAdjacencyList()) {
                        if (field instanceof GoalField) {
                            fieldsToMove.add(field);
                            actualField = field;
                        }
                    }
                } else {
                    for (Field field : actualField.getAdjacencyList()) {
                        if (!(field instanceof GoalField)) {
                            fieldsToMove.add(field);
                            actualField = field;
                        }
                    }
                }
            }
            else {
                for (Field field : actualField.getAdjacencyList()) {
                        fieldsToMove.add(field);
                        actualField = field;
                    }
                }
            }
        int distance = fieldsToMove.size();
        for (Field field : fieldsToMove) {
            this.move(game, occ, field);
        }
        return remaining - distance;
    }

    public void move(Game game, Figure figure, Field targetFieldObject) {

        Field targetField = this.matchFields(game, targetFieldObject);
        Field currentField = getFieldfromFigure(game, figure);
        Figure occ = currentField.getOccupant();
        if (occ == null) {
            System.out.println();
        }
        assert occ != null;

        if (targetField.getOccupant() != null) {
            Figure occupant = targetField.getOccupant();
            this.sendFigureHome(game, occupant);
            currentField.setOccupant(null);
            targetField.setOccupant(occ);
            occ.setField(targetField);

        }
        else {
            currentField.setOccupant(null);
            targetField.setOccupant(occ);
            occ.setField(targetField);

        }
        if (targetField instanceof FirstField && currentField instanceof HomeField) {
            ((FirstField) targetField).setBlocked(true);
        }
        if (currentField instanceof FirstField) {
            ((FirstField) currentField).setBlocked(false);
        }
    }

    public boolean checkIfAllTargetFieldsOccupied(Game game, Player player) {
        Board board = this.getBoard(game);
        List<Field> fieldsOfBoard = board.getFields();
        int count = 0;
        for (Field field : fieldsOfBoard) {
            if (field instanceof GoalField && ((GoalField) field).getPlayer() == player && field.getOccupant() != null) {
                count++;
            }
        }
        return count == 4;
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
    public ArrayList<Field> getPossibleFields(Game actualGame, Card card, Field field) {
        Board board = actualGame.getBoard();
        ArrayList<Integer> moveValues = getMoveValues(card);

        if(field instanceof HomeField && (card.getValue() == Value.KING || card.getValue() == Value.ACE)){
            Player playerOfField = ((HomeField) field).getPlayer();
            for (Field key : board.getFields()){
                if(key instanceof FirstField && ((FirstField) key).getPlayer().getId()==playerOfField.getId() &&
                !(((FirstField) key).getBlocked())){
                    ArrayList<Field> fields = new ArrayList<>();
                    fields.add(key);
                    return fields;
                }
            }
        }
        if(field instanceof HomeField) {
            return new ArrayList<>();
        }
        if (card.getValue() == Value.FOUR) {
            ArrayList<Field> forwardFour = getFieldsBoard(field, moveValues);
            ArrayList<Field> backwardFour = getPossibleFieldsFour(card, field, board);
            backwardFour.removeIf(n -> (n instanceof GoalField));
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
                if (temp instanceof FirstField || temp instanceof CasualField) {
                    for (Field boardField : fields) {
                        if (boardField.getAdjacencyList().contains(temp)) {
                            if (boardField instanceof HomeField) {
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
                            else {
                                queue.add(boardField);
                            }
                        }
                    }
                } else if (temp instanceof GoalField){
                    queue.addAll(temp.getAdjacencyList());
                }
                    if(temp instanceof FirstField){
                        for(Field pField : temp.getAdjacencyList()){
                            if(pField instanceof GoalField && pField.getOccupant() == null){
                                if(((GoalField) pField).getPlayer()==player){
                                    queue.add(pField);
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
            if (field instanceof HomeField) {
                return possibleFields;
        }
        for(int i=1; i<=value;i++) {
            ArrayList<Integer> values = new ArrayList<>();
            values.add(i);
            possibleFields.addAll(this.getFieldsBoard(field, values));
        }
        return possibleFields;
    }

    public ArrayList<Field> getFieldsBoard(Field fieldToCheck, ArrayList<Integer> valuesToCheck){
        ArrayList<Field> possibleFields = new ArrayList<>();
        for(int moveValue : valuesToCheck) {
            int level = 0;
            Queue<Field> queue = new LinkedList<>();
            queue.add(fieldToCheck);
            queue.add(null);
            while (!queue.isEmpty() && level < moveValue) {
                Field temp = queue.poll();
                if (temp == null) {
                    level++;
                    queue.add(null);
                }
                else {
                    List<Field> adjFields = temp.getAdjacencyList();
                    List<Field> adjFieldsCopyFirstState = new ArrayList<>(adjFields);
                    if (temp instanceof FirstField && ((FirstField) temp).getBlocked()) {
                        for (Field field1 : adjFieldsCopyFirstState) {
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
                        else if (f instanceof GoalField && ((GoalField) f).getPlayer() == fieldToCheck.getOccupant().getPlayer() && f.getOccupant() != null) {
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

    public ArrayList<Field> getPossibleFieldsJack(Game actualGame, Card card, Field field){
        ArrayList<Field> possibleFields = new ArrayList<>();
        Player playerOnField = field.getOccupant().getPlayer();
        Board board = actualGame.getBoard();

        for(Field iterField : board.getFields()){
            if(iterField.getOccupant()!=null){
                if(iterField.getOccupant().getPlayer().getId()!=playerOnField.getId() && (iterField instanceof CasualField
                        || (iterField instanceof FirstField && !(((FirstField) iterField).getBlocked())))){
                    possibleFields.add(iterField);
                }
            }
        }
        return possibleFields;
    }

}