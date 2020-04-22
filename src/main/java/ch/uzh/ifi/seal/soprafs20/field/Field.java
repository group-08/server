package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.User.Figure;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "FIELD")
public abstract class Field {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(targetEntity = Figure.class)
    private Figure occupant;

    @OneToMany(targetEntity = Field.class)
    private List<Field> adjacencyList = new ArrayList<>();

    public void addAdjacency(Field adjacentField){
        if (!this.adjacencyList.contains(adjacentField)) {
            this.adjacencyList.add(adjacentField);
        }
    }

    public void removeAdjacency(Field removeField) {
        if (!this.adjacencyList.contains(removeField)) {
            this.adjacencyList.remove(removeField);
        }
    }

    public List<Field> getAdjacentFields() {
        return this.adjacencyList;
    }

    public Figure getOccupant() {
        return occupant;
    }

    public void setOccupant(Figure occupant){
        return;
    }

    public long getId(){return id;};

    public boolean equals(Object v){
        if (v==this){
            return true;
        }
        if(!(v instanceof Field)){
            return false;
        }

        Field v2 = (Field) v;

        return id == v2.id;
    }

    public int hashCode() {
        int result = 17;
        result = (int) (31 * result + id);
        return result;
    }

    @OneToOne(mappedBy = "field", optional = false)
    private Figure figure;

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}
