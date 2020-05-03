package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.user.Player;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "GOALFIELD")
public class GoalField extends Field{

    @OneToOne
    private Player player;

    // supposed to override getOccupant or new method?
    public Player getPlayer(){
        return player;
    }

}

