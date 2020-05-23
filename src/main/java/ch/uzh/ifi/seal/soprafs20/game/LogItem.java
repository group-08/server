package ch.uzh.ifi.seal.soprafs20.game;


import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;


import javax.persistence.*;

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

    public LogItem(){}

    public LogItem(Suit suit, Value value, long playerId){
        this.suit=suit;
        this.playerId=playerId;
        this.value=value;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
