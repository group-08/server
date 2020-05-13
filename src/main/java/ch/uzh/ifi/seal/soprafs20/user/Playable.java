package ch.uzh.ifi.seal.soprafs20.user;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

public interface Playable {

    public void setHand(List<Card> cards);

    public List<Card> getHand();

    public void addFigure(Figure figure);

    public List<Figure> getFigures();

    public long getId();

    public void setColour(Colour colour);

    public Colour getColour();


}
