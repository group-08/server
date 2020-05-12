package ch.uzh.ifi.seal.soprafs20.field;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table( name = "CASUALFIELD")
public class CasualField extends Field {
    public CasualField() {
        this.colour = null;
    }
}
