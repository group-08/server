package ch.uzh.ifi.seal.soprafs20.User;


import javax.persistence.*;
import ch.uzh.ifi.seal.soprafs20.field.Field;

@Entity
@Table(name = "FIGURE")



public class Figure {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(targetEntity = Player.class)
    Player player;
    Field field;


    public long getId(){
        return this.id;
    }

    public Player getPlayer() { return this.player; }

    public Field getField() { return this.field; }
}
