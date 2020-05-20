package ch.uzh.ifi.seal.soprafs20.game;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Player.class, cascade = CascadeType.ALL)
    List<Player> players;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Card.class, cascade = CascadeType.ALL)
    List<Card> cards;

    LogItem(){
        players = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
