package ch.uzh.ifi.seal.soprafs20.User;

import ch.uzh.ifi.seal.soprafs20.cards.Card;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYER")
public class Player {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(targetEntity = User.class)
    User user;

    @Column
    Colour colour;

    @OneToMany(targetEntity = Card.class)
    List<Card> hand;

    @OneToMany(targetEntity = Figure.class)
    List<Figure> figures;


}
