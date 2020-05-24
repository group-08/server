package ch.uzh.ifi.seal.soprafs20.cards;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Card.class, cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();



    public Deck(){
        this.cards = new ArrayList<>();

    }


    public List<Card> getCardsInDeck() {
        return this.cards;
    }

    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    public Long getId(){
        return this.id;
    }
}
