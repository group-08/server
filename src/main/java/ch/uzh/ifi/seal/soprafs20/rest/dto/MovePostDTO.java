package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

public class MovePostDTO {

    private Card card;

    private Figure figure;

    private Field targetField;

    private int remainingSeven;

    public void setRemainingSeven(int remaining) {
        this.remainingSeven = remaining;
    }

    public int getRemainingSeven() {
        return this.remainingSeven;
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
