package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardGetDTO {

    private long id;

    private List<FieldGetDTO> fields;

    private List<Player> players;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FieldGetDTO> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        List<FieldGetDTO> fieldGetDTOS = new ArrayList<>();
        for (Field field : fields) {
            fieldGetDTOS.add(DTOMapper.INSTANCE.convertEntityToFieldGetDTO(field));
        }
        this.fields = fieldGetDTOS;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
