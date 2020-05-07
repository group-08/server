package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

public class MovePostDTO {

    private long id;

    private Card card;

    private Figure figure;

    private Field targetField;

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

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {this.targetField = targetField;}

}
