package ch.uzh.ifi.seal.soprafs20.user;


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

    @OneToOne(targetEntity = Field.class)
    Field field;

    public Figure(Field field) {
        this.field = field;
    }

    public void moveToNewField(Field targetField){
        try {
            this.field = targetField;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.printf("invalid field exeption");
            return;
        }

    }

    public long getId(){
        return this.id;
    }

    public Player getPlayer() { return this.player; }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Field getField() { return this.field; }

}