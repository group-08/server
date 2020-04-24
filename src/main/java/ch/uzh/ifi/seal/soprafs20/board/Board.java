package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.Field;

import java.io.Serializable;
import javax.persistence.*;

import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Entity
@Table(name = "BOARD")
public abstract class Board implements Serializable {

    @GeneratedValue
    @Id
    private long id;

    @OneToMany(targetEntity = Field.class)
    private List<Field> fields = new ArrayList<>();;

    @OneToMany(targetEntity = Player.class)
    private List<Player> players = new ArrayList<>();

    protected int version;

    public Board() {
        // Create all the fields
        for (int i = 0; i < 64; i++){ // Casual fields
            this.fields.add(new CasualField());
        }
        for (int i = 64; i < 80; i++){ // Goal field
            this.fields.add(new CasualField());
        }
        for (int i = 80; i < 96; i++){ // Home field
            this.fields.add(new CasualField());
        }

        // Add the graphs to all the field
        this.createGraphs(this.fields);

    }

    private void createGraphs(List<Field> fields) {
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
        /* sends figure home */
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
        return this;
    }

    /**
     * Get the possible field so we can either automatically move the piece or display all possible fields
     * @param card the card that was played
     * @param field field the card is being palyed on
     * @return List of all possible fields the player on field could land on
     */
    public ArrayList<Field> getPossibleFields(Card card, Field field) {
        int moveValue = card.getValue().getValue();
        ArrayList<Field> possibleFields = new ArrayList<>();


        if(field instanceof HomeField && card.getValue() == Value.KING || card.getValue() == Value.ACE){
            Player playerOfField = ((HomeField) field).getPlayer();
            for (Field key : this.fields){
                if(key instanceof FirstField && ((FirstField) key).getPlayer()==playerOfField){
                    ArrayList<Field> fields = new ArrayList<>();
                    fields.add(key);
                    return fields;
                }
            }
        }
        int level = 0;
        Queue<Field> queue = new LinkedList<>();
        queue.add(field);
        queue.add(null);
        while(!queue.isEmpty() && level<moveValue){
            Field temp = queue.poll();
            if(temp==null){
                level++;
                queue.add(null);
            }
            else{
                List<Field> adjFields= temp.getAdjacentFields();
                if(temp instanceof FirstField && ((FirstField) temp).getBlocked()){
                    for(Field field1 : adjFields){
                        if (field1 instanceof GoalField){
                            adjFields.remove(field1);
                        }
                    }
                }
                for(Field f: adjFields){
                    if(f instanceof FirstField && ((FirstField) f).getBlocked()) {
                        assert true;
                    }
                    else if (f instanceof GoalField && ((GoalField) f).getPlayer()!=field.getOccupant().getPlayer()){
                        assert true;
                    }
                    else{
                        queue.add(f);
                    }
                }
            }
        }
        ArrayList<Field> fields = new ArrayList<>();
        while(!queue.isEmpty()) {
            if (queue.peek() != null) {
                fields.add(queue.poll());
            }
            else {
                queue.poll();
            }
        }

        return possibleFields;
    }

}
