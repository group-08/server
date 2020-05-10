package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="CardType")
@Table(name="Card")
public abstract class Card implements Serializable {
    //Has to be abstract
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated
    protected Value value;

    @Enumerated
    protected CardType type;

    public abstract String toString();

    public Value getValue() {
        return this.value;
    }

    public CardType getType() {
        return this.type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
