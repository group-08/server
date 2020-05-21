package ch.uzh.ifi.seal.soprafs20.game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOGITEM")
public class LogItem {

    @Id
    @GeneratedValue
    private Long id;


    @Column
    Long playerId;

    @Enumerated
    Suit suit;

    @Enumerated
    Value value;

    public LogItem(){};

    public LogItem(Suit suit, Value value, long playerId){
        this.suit=suit;
        this.playerId=playerId;
        this.value=value;
    }

    public long getPlayer() {
        return playerId;
    }

    public void setPlayer(long playerId) {
        this.playerId = playerId;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
