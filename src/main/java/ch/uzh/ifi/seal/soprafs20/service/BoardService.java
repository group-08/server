package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.*;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PDLOverrideSupported;
import javax.transaction.Transactional;
import java.util.*;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;
    private Random random;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        @Qualifier("gameRepository") GameRepository gameRepository){
        this.boardRepository = boardRepository;
        this.gameRepository = gameRepository;
        this.random = new Random();
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

    public void blowFigureAway(Game game, Figure figure){
        List<Long> specialFieldIds = getSpecialFieldIds(game);
        Board board = this.getBoard(game);
        Field currentField = this.getFieldfromFigure(game, figure);
        Figure currentFigure = currentField.getOccupant();
        List<Field> casualFields = new ArrayList<>();
        for(Field field : board.getFields()){
            if(field instanceof CasualField && !specialFieldIds.contains(field.getId())) {
                casualFields.add(field);
            }
        }

        Field randomCasualField = casualFields.get(random.nextInt(casualFields.size()));

        if(randomCasualField.getOccupant()!=null){
            Figure figureOfField = randomCasualField.getOccupant();
            sendFigureHome(game, figureOfField);
        }
        currentField.setOccupant(null);
        randomCasualField.setOccupant(currentFigure);
        currentFigure.setField(randomCasualField);
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

        List<Long> specialFieldIds = getSpecialFieldIds(game);

        if(specialFieldIds.contains(targetFieldObject.getId())) {
            if (game.getWeatherState() == WeatherState.RAINY) {
                sendFigureHome(game, occ);
            }
            if (game.getWeatherState() == WeatherState.WINDY) {
                blowFigureAway(game, occ);
            }
        }

        return remaining - distance;
    }

    public void move(Game game, Figure figure, Field targetFieldObject) {

        Field targetField = this.matchFields(game, targetFieldObject);
        Field currentField = getFieldfromFigure(game, figure);
        Figure occ = currentField.getOccupant();
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

        List<Long> specialFieldIds = getSpecialFieldIds(game);

        if(specialFieldIds.contains(targetField.getId())) {
            if (game.getWeatherState() == WeatherState.RAINY) {
                sendFigureHome(game, occ);
            }
            if (game.getWeatherState() == WeatherState.WINDY) {
                blowFigureAway(game, occ);
            }
        }
    }

    public List<Long> getSpecialFieldIds(Game game){
        List<Field> fields = game.getBoard().getFields();
        List<Long> specialFields = new ArrayList<>();
        specialFields.add(fields.get(8).getId());
        specialFields.add(fields.get(24).getId());
        specialFields.add(fields.get(40).getId());
        specialFields.add(fields.get(56).getId());
        return specialFields;
    }

    public void checkFieldsWeatherChange(Game game){
        Board board = game.getBoard();
        List<Long> specialFieldIds = getSpecialFieldIds(game);

        for(Field field : board.getFields()){
            if(field.getOccupant() != null && specialFieldIds.contains(field.getId())){
                Figure figure = field.getOccupant();
                if(game.getWeatherState() == WeatherState.WINDY){
                    blowFigureAway(game, figure);
                }
                if(game.getWeatherState() == WeatherState.RAINY){
                    sendFigureHome(game,figure);
                }
            }
        }
    }

    public boolean checkIfAllTargetFieldsOccupied(Game game, Player player) {
        Board board = this.getBoard(game);
        List<Field> fieldsOfBoard = board.getFields();
        int count = 0;
        for (Field field : fieldsOfBoard) {
            if (field instanceof GoalField && ((GoalField) field).getPlayer().getId() == player.getId() && field.getOccupant() != null) {
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
                                if(((GoalField) pField).getPlayer()==player && !((FirstField) temp).getBlocked()){
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

    public ArrayList<Field> getPossibleFieldsJoker(Game actualGame,  Field field){
        Card card2 = new NormalCard(Suit.CLUBS, Value.TWO);
        Card card3 = new NormalCard(Suit.CLUBS, Value.THREE);
        //four is special
        Card card4 = new NormalCard(Suit.CLUBS, Value.FOUR);

        Card card5 = new NormalCard(Suit.CLUBS, Value.FIVE);
        Card card6 = new NormalCard(Suit.CLUBS, Value.SIX);


        Card card7 = new NormalCard(Suit.CLUBS,Value.SEVEN);

        Card card8 = new NormalCard(Suit.CLUBS, Value.EIGHT);
        Card card9 = new NormalCard(Suit.CLUBS, Value.NINE);
        Card card10 = new NormalCard(Suit.CLUBS, Value.TEN);
        //jack is special
        Card cardJack = new NormalCard(Suit.CLUBS, Value.JACK);

        Card cardQueen = new NormalCard(Suit.CLUBS, Value.QUEEN);
        Card cardAce = new NormalCard(Suit.CLUBS, Value.ACE);
        Card cardKing = new NormalCard(Suit.CLUBS, Value.KING);

        ArrayList<Field> possibleMovesJoker = new ArrayList<>();

        ///normal cards valid moves
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card2,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card3,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card5,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card6,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame,card7, field));

        possibleMovesJoker.addAll(getPossibleFields(actualGame, card8,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card9,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card10,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, cardQueen,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, cardAce,field));
        possibleMovesJoker.addAll(getPossibleFields(actualGame, cardKing,field));





        ///fours valid moves
        possibleMovesJoker.addAll(getPossibleFields(actualGame, card4, field));

        ///Jacks valid move
        possibleMovesJoker.addAll(getPossibleFieldsJack(actualGame,cardJack,field));


        return possibleMovesJoker;

    }



    public ArrayList<Field> getPossibleFieldsSeven(Card card, Field field){
        ArrayList<Field> possibleFields = new ArrayList<>();
        int remaining = card.getRemainingSteps();
            if (field instanceof HomeField) {
                return possibleFields;
        }
        for(int i=1; i<=remaining;i++) {
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
                    List<Field> adjFields = new ArrayList<>(temp.getAdjacencyList());
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
                        else if (f instanceof GoalField &&
                                ((GoalField) f).getPlayer().getId() != fieldToCheck.getOccupant().getPlayer().getId()) {
                            assert true;
                        }
                        else if (f instanceof GoalField &&
                                ((GoalField) f).getPlayer().getId() == fieldToCheck.getOccupant().getPlayer().getId() && f.getOccupant() != null) {
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
        if(field instanceof FirstField){
            if(((FirstField) field).getBlocked()){
                return possibleFields;
            }
        }
        if(field instanceof HomeField){
            return possibleFields;
        }

        if(field instanceof GoalField){
            return possibleFields;
        }

        for(Field iterField : board.getFields()){
            if(iterField.getOccupant()!=null){
                if(iterField.getOccupant().getPlayer().getId()!=playerOnField.getId()
                        && (iterField instanceof CasualField
                        || (iterField instanceof FirstField && !(((FirstField) iterField).getBlocked())))){
                    possibleFields.add(iterField);
                }
            }
        }
        return possibleFields;
    }

}