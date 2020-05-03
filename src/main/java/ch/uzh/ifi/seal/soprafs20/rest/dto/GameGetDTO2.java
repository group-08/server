package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.game.GameState;
import ch.uzh.ifi.seal.soprafs20.game.WeatherState;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import ch.uzh.ifi.seal.soprafs20.user.User;

import java.util.List;

public class GameGetDTO2 {
    private String name;

    private Long id;

    private GameState gameState;

    private User host;

    private List<Player> players;

    private boolean exchangeCard;

    private WeatherState weatherState;

    private int cardNum;

    private Board board;

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

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean isExchangeCard() {
        return exchangeCard;
    }

    public void setExchangeCard(boolean exchangeCard) {
        this.exchangeCard = exchangeCard;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
