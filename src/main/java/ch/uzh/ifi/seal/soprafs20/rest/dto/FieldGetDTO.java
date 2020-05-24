package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

public class FieldGetDTO {

    private long id;

    private Figure occupant;

    private Colour colour;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FigureGetDTO getOccupant() {
        return DTOMapper.INSTANCE.convertEntityToFigureGetDTO(this.occupant);
    }

    public void setOccupant(Figure occupant) {
        this.occupant = occupant;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
}
