package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.board.CasualBoard;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;

import java.util.ArrayList;
import java.util.List;

public class LobbyGetDTO {

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

    public List<PlayerGetDTO> getPlayers() {
        List<PlayerGetDTO> playerGetDTOList = new ArrayList<>();
        for (Player player : this.players) {
            playerGetDTOList.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }
        return playerGetDTOList;
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
