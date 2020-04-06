package ch.uzh.ifi.seal.soprafs20.board;

import ch.uzh.ifi.seal.soprafs20.User.Player;
import ch.uzh.ifi.seal.soprafs20.field.Field;

import java.io.Serializable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "BOARD")
public abstract class Board implements Serializable {

    @Id
    private long id;

    @OneToMany(targetEntity = Field.class)
    private Collection<Field> fields;

    @OneToMany(targetEntity = Player.class)
    private List<Player> players;

    public void play(){
        /* something */
    }


}
