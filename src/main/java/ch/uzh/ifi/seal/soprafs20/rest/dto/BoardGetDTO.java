package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.List;

public class BoardGetDTO {

    private long id;

    private List<Field> fields;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FieldGetDTO> getFields() {
        List<FieldGetDTO> fieldGetDTOS = new ArrayList<>();
        for (Field field : this.fields) {
            fieldGetDTOS.add(DTOMapper.INSTANCE.convertEntityToFieldGetDTO(field));
        }
        return fieldGetDTOS;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
