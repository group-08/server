package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.User.Figure;

import javax.persistence.*;

@Entity
@Table( name = "FIELD")
public abstract class Field {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(targetEntity = Figure.class)
    private Figure occupant;

    public Figure getOccupant() {
        return occupant;
    }

    public void setOccupant(Figure occupant){ }
}
