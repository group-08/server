package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;

import java.util.List;

public class GameGetDTO {

    private String name;

    private Long id;

    private GameState gameState;

    private User host;

    private List<Player> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
         this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public UserGetDTO getHost() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(host);
    }
}
