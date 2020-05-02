package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "CARD")
public abstract class Card implements Serializable {
    //Has to be abstract
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public abstract String toString();

    public Value getValue(){
        return null;
    }
}
