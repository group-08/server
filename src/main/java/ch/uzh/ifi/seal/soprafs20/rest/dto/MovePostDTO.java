package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

public class MovePostDTO {

    private long cardId;

    private long figureId;

    private long targetFieldId;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getFigureId() {return figureId;}

    public void setFigureId(long figureId){ this.figureId=figureId;}

    public long getTargetFieldId() {
        return targetFieldId;
    }

    public void setTargetFieldId(long targetFieldId) {this.targetFieldId = targetFieldId;}

}
