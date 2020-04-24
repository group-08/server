package ch.uzh.ifi.seal.soprafs20.field;


import ch.uzh.ifi.seal.soprafs20.User.Player;

public class GoalField extends Field {

    private Player player;



    // supposed to override getOccupant or new method?
    public Player getPlayer(){
        return player;
    }

}

