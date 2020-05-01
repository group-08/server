package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.user.Player;

public class FirstField extends Field {

    private Player player;
    private Boolean blocked;

    // suppoed to overide getOccupant or ned method?
    public Player getPlayer(){ return player;}
    public void setPlayer(Player player){this.player=player;}
    public boolean getBlocked(){ return blocked;}
    public void setBlocked(boolean blocked){this.blocked=blocked;}
}

