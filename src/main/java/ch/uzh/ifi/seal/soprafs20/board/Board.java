package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.field.*;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "BOARD")
public class Board implements Serializable {

    @GeneratedValue
    @Id
    private long id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Field.class, cascade = CascadeType.ALL)
    private List<Field> fields = new ArrayList<>();


    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Player.class, cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @Column
    long passedTime;

    public Board() {
        // Create all the fields
        this.passedTime = System.currentTimeMillis()/1000;

        for (int i = 0; i <= 63; i++){ // Casual fields
            if(i==0 || i==16 || i==32 || i==48){
                FirstField firstField = new FirstField();
                firstField.setBlocked(false);
                this.fields.add(firstField);
            }
            else {
                this.fields.add(new CasualField());
            }
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
        for(int index=0; index<63;index++){
            Field first = fields.get(index);
            Field second = fields.get(index+1);
            first.addAdjacency(second);
        }

        for(int index=64; index<67;index++){
            Field first = fields.get(index);
            Field second = fields.get(index+1);
            first.addAdjacency(second);
        }
        for(int index=68; index<71;index++){
            Field first = fields.get(index);
            Field second = fields.get(index+1);
            first.addAdjacency(second);
        }
        for(int index=72; index<75;index++){
            Field first = fields.get(index);
            Field second = fields.get(index+1);
            first.addAdjacency(second);
        }
        for(int index=76; index<79;index++){
            Field first = fields.get(index);
            Field second = fields.get(index+1);
            first.addAdjacency(second);
        }
        for(int index=80; index<84;index++){
            Field first = fields.get(index);
            Field second = fields.get(0);
            first.addAdjacency(second);
        }
        for(int index=84; index<88;index++){
            Field first = fields.get(index);
            Field second = fields.get(16);
            first.addAdjacency(second);
        }
        for(int index=88; index<92;index++){
            Field first = fields.get(index);
            Field second = fields.get(32);
            first.addAdjacency(second);
        }
        for(int index=92; index<96;index++){
            Field first = fields.get(index);
            Field second = fields.get(48);
            first.addAdjacency(second);
        }

        fields.get(63).addAdjacency(fields.get(0));
        fields.get(0).addAdjacency(fields.get(64));
        fields.get(16).addAdjacency(fields.get(68));
        fields.get(32).addAdjacency(fields.get(72));
        fields.get(48).addAdjacency(fields.get(76));

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

    public Field getField(int fieldId) {
        return this.fields.get(fieldId - 1); // Minus one so we respect the bounds
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public long getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(long passedTime) {
        this.passedTime = passedTime;
    }
}

