package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.field.*;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "BOARD")
public class Board implements Serializable {

    @GeneratedValue
    @Id
    private long id;

    @OneToMany(targetEntity = Field.class, cascade = CascadeType.ALL)
    private List<Field> fields = new ArrayList<>();


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

}

