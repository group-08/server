package ch.uzh.ifi.seal.soprafs20.cards;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "DiscardPile")
public  class DiscardPile implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(targetEntity = Card.class)
    List<Card> cards = new ArrayList<Card>();
}
