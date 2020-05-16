package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.game.CityState;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;

import java.util.ArrayList;
import java.util.List;

public class GameGetDTO {
    private Long id;

    private String name;

    private GameState gameState;

    private User host;

    private List<Player> players;

    private Board board;

    private WeatherState weatherState;

    private CityState cityState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public UserGetDTO getHost() {
        UserGetDTO hostDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(this.host);
        return hostDTO;
    }

    public void setHost(User host) {
        this.host = host;
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

    public BoardGetDTO getBoard() {
        BoardGetDTO boardGetDTO = DTOMapper.INSTANCE.convertEntityToBoardGetDTO(this.board);
        return boardGetDTO;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }

    public CityState getCityState() {
        return cityState;
    }

    public void setCityState(CityState cityState) {
        this.cityState = cityState;
    }
}
