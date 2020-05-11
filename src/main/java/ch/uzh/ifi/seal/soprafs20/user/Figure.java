package ch.uzh.ifi.seal.soprafs20.user;


import ch.uzh.ifi.seal.soprafs20.field.Field;

import javax.persistence.*;

@Entity
@Table(name = "FIGURE")



public class Figure {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(targetEntity = Player.class, cascade = CascadeType.ALL)
    Player player;

    @OneToOne(targetEntity = Field.class, cascade = CascadeType.ALL)
    Field field;

    public Figure() {}

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

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() { return this.player; }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Field getField() { return this.field; }

    public void setField(Field field) {
        this.field = field;
    }

}
