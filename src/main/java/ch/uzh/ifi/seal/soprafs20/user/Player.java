package ch.uzh.ifi.seal.soprafs20.user;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(targetEntity = Card.class, cascade = CascadeType.ALL)
    List<Card> hand;

    @OneToMany(targetEntity = Figure.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Figure> figures;

    public Player() {
        this.figures = new ArrayList<Figure>();
    }

    public void setHand(List<Card> cards) {
        this.hand = cards;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGetDTO getUser() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(this.user);
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public void addFigure(Figure figure) {
        this.figures.add(figure);
    }

    public List<Figure> getFigures() {
        return this.figures;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return this.colour;
    }

}

