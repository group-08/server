package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.user.Player;

import javax.persistence.*;

@Entity
@Table( name = "GOALFIELD")
public class HomeField extends Field{
    @OneToOne
    private Player player;

    public Player getPlayer(){return player;}

    public void setPlayer(Player player) {
        this.player = player;
    }
}
