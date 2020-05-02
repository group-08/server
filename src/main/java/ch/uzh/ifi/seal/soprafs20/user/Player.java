package ch.uzh.ifi.seal.soprafs20.user;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import javax.persistence.*;
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


}
