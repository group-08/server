package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.User.Figure;
import ch.uzh.ifi.seal.soprafs20.cards.Card;

public class MovePostDTO {

    private long id;

    private Card card;

    private Figure figure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Figure getFigure() {return figure;}

    public void setFigure(Figure figure){ this.figure=figure;}
}
