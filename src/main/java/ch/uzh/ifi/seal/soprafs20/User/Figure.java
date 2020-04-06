package ch.uzh.ifi.seal.soprafs20.User;

import javax.persistence.*;

@Entity
@Table(name = "FIGURE")
public class Figure {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(targetEntity = Player.class)
    Player player;
}
