package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(targetEntity = Card.class)
    List<Card> cards = new ArrayList<Card>();

    public List<Card> deal(int amount){
        return null;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

}
