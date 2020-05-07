package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.user.Player;

import javax.persistence.*;

@Entity
@Table( name = "FIRSTFIELD")
public class FirstField extends Field {
    @OneToOne
    private Player player;

    private Boolean blocked;

    public Player getPlayer(){ return player;}
    public void setPlayer(Player player){this.player=player;}
    public boolean getBlocked(){ return blocked;}
    public void setBlocked(boolean blocked){this.blocked=blocked;}


}

