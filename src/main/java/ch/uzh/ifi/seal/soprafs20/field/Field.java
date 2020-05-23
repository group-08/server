package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="FieldType")
@Table(name="Field")
public abstract class Field implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(targetEntity = Figure.class, cascade = CascadeType.ALL)
    private Figure occupant;

    @Column
    protected Colour colour;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(targetEntity = Field.class, cascade = CascadeType.ALL)
    private List<Field> adjacencyList = new ArrayList<>();


    public void addAdjacency(Field adjacentField){
        if (!this.adjacencyList.contains(adjacentField)) {
            this.adjacencyList.add(adjacentField);
        }
    }



    public List<Field> getAdjacencyList() {
        return this.adjacencyList;
    }



    public Figure getOccupant() {
        return occupant;
    }

    public void setOccupant(Figure occupant){
        this.occupant = occupant;
    }

    public long getId(){return id;}

    public void setId(long id) {
        this.id = id;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

}
