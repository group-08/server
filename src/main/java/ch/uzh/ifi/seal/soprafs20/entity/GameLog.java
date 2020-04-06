package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "GAMELOG")
public class GameLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int version;

    @Column
    private ArrayList<Board> boards;

    @Column
    private ArrayList<Card> cards;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {return version;}

    public void setVersion(int version) {this.version = version;}

    public ArrayList<Card> getCards() {return cards;}

    public void setCards(ArrayList<Card> cards){this.cards=cards;}

    public ArrayList<Board> getBoards() {return boards;}

    public void setBoards(ArrayList<Board> boards){this.boards=boards;}


    //save the boards in an ArrayList for every new board
    //if we have multiple games being played they can all be identified by the id


}
