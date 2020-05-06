package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;

public class FieldGetDTO {

    private long id;

    private Figure occupant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FigureGetDTO getOccupant() {
        FigureGetDTO occupantDTO = DTOMapper.INSTANCE.convertEntityToFigureGetDTO(this.occupant);
        return occupantDTO;
    }

    public void setOccupant(Figure occupant) {
        this.occupant = occupant;
    }
}
