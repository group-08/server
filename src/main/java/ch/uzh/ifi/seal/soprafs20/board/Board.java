package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.Field;

import java.io.Serializable;
import javax.persistence.*;

import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.*;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;


@Entity
@Table(name = "BOARD")
public abstract class Board implements Serializable {

    @GeneratedValue
    @Id
    private long id;

    @OneToMany(targetEntity = Field.class)
    private List<Field> fields = new ArrayList<>();
    ;

    @OneToMany(targetEntity = Player.class)
    private List<Player> players = new ArrayList<>();

    @Transient
    @Autowired
    private BoardRepository boardRepository;


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public BoardRepository getBoardRepository() {
        return boardRepository;
    }

    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board() {


        // Create all the fields
        for (int i = 0; i <= 63; i++){ // Casual fields
            this.fields.add(new CasualField());
        }
        for (int i = 64; i <= 79; i++){ // Goal field
            this.fields.add(new GoalField());
        }
        for (int i = 80; i <= 95; i++){ // Home field
            this.fields.add(new HomeField());
        }

        // Add the graphs to all the field
        this.forwardGraph(this.fields);

        // Create Figures
        for (int i=80; i<=95; i++) {
            Field field = fields.get(i);
            Figure figure = new Figure(field);
            field.setOccupant(figure);
        }
    }

    private void forwardGraph(List<Field> fields) {
        for(int id=0; id<63;id++){
            Field first = fields.get(id);
            Field second = fields.get(id+1);
            first.addAdjacency(second);
        }

        for(int id=64; id<67;id++){
            Field first = fields.get(id);
            Field second = fields.get(id+1);
            first.addAdjacency(second);
        }
        for(int id=68; id<71;id++){
            Field first = fields.get(id);
            Field second = fields.get(id+1);
            first.addAdjacency(second);
        }
        for(int id=72; id<75;id++){
            Field first = fields.get(id);
            Field second = fields.get(id+1);
            first.addAdjacency(second);
        }
        for(int id=76; id<79;id++){
            Field first = fields.get(id);
            Field second = fields.get(id+1);
            first.addAdjacency(second);
        }
        for(int id=80; id<84;id++){
            Field first = fields.get(id);
            Field second = fields.get(0);
            first.addAdjacency(second);
        }
        for(int id=84; id<88;id++){
            Field first = fields.get(id);
            Field second = fields.get(16);
            first.addAdjacency(second);
        }
        for(int id=88; id<92;id++){
            Field first = fields.get(id);
            Field second = fields.get(32);
            first.addAdjacency(second);
        }
        for(int id=92; id<96;id++){
            Field first = fields.get(id);
            Field second = fields.get(48);
            first.addAdjacency(second);
        }

        fields.get(63).addAdjacency(fields.get(0));
        fields.get(0).addAdjacency(fields.get(64));
        fields.get(16).addAdjacency(fields.get(68));
        fields.get(32).addAdjacency(fields.get(72));
        fields.get(48).addAdjacency(fields.get(76));

    }

    public Field getField(int id) {
        return this.fields.get(id);
    }

    public List<Player> getAllPlayers(Long id){
        Board boardByID = boardRepository.findById(id).orElse(null);
        if(boardByID!=null){
            return boardByID.players;
        }
        else{
            return null;
        }
    }

    /**
     * Takes a Figure and gets Figures current Field
     * @param figure figure you want the current field of
     * @return the current field of the figure
     */
    public Field getCurrentField(Figure figure) {
        return figure.getField();
    }

    /**
     * Sends the figure home
     * @param figure that has to be sent home
     */
    public void sendFigureHome(Figure figure) {
        Player playerOfFigure = figure.getPlayer();
        for(Field field : this.fields){
            if(field instanceof HomeField){
                if(((HomeField) field).getPlayer()==playerOfFigure && field.getOccupant() == null){
                    field.setOccupant(figure);
                    return;
                }
            }
        }
    }


    public Board move(Figure figure, Field targetField) {
        Field currentField = this.getCurrentField(figure);
        if (targetField.getOccupant() != null) {
            Figure occupant = targetField.getOccupant();
            this.sendFigureHome(occupant);
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

        boardRepository.saveAndFlush(this);
        return this;
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

    public boolean checkIfAllTargetFieldsOccupied(Long id, Player player) {
        List<Field> fieldsOfBoard = this.fields;
        int count = 0;
        for (Field field : fieldsOfBoard) {
            if (field instanceof GoalField && ((GoalField) field).getPlayer() == player && field.getOccupant() != null) {
                count++;
            }
        }
        return count == 4;
    }

    /**
     * Get the possible field so we can either automatically move the piece or display all possible fields
     * @param card the card that was played
     * @param field field the card is being palyed on
     * @return List of all possible fields the player on field could land on
     */


    public ArrayList<Field> getPossibleFields(Card card, Field field) {
        ArrayList<Integer> moveValues = getMoveValues(card);

        if(field instanceof HomeField && (card.getValue() == Value.KING || card.getValue() == Value.ACE)){
            Player playerOfField = ((HomeField) field).getPlayer();
            for (Field key : this.fields){
                if(key instanceof FirstField && ((FirstField) key).getPlayer()==playerOfField){
                    ArrayList<Field> fields = new ArrayList<>();
                    fields.add(key);
                    return fields;
                }
            }
        }
        return getFields(field, moveValues);
    }


    public ArrayList<Field> getPossibleFieldsSeven(Card card, Field field, int value){
        ArrayList<Integer> values = new ArrayList<>();
        for(int i=1; i<=value;i++) {
            values.add(i);
        }
        return getFields(field, values);
    }

        public ArrayList<Field> getPossibleFieldsJack(Card card, Field field){
            ArrayList<Field> possibleFields = new ArrayList<>();
            Player playerOnField = field.getOccupant().getPlayer();

            for(Field iterField : this.fields){
                if(iterField.getOccupant()!=null){
                    if(iterField.getOccupant().getPlayer()!=playerOnField && (iterField instanceof CasualField
                    || (iterField instanceof FirstField && !(((FirstField) iterField).getBlocked())))){
                        possibleFields.add(iterField);
                    }
                }
            }
            return possibleFields;
        }

        private ArrayList<Field> getFields(Field fieldToCheck, ArrayList<Integer> valuesToCheck){
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


}

