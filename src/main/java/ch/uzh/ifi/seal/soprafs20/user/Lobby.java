package ch.uzh.ifi.seal.soprafs20.user;

import ch.uzh.ifi.seal.soprafs20.game.Game;

import java.util.ArrayList;

public class Lobby {
    ArrayList<Game> games;
    ArrayList<User> users;

    public void createGame(){} //returns game?
    public void deleteGame(){}
    public void createPlayer(User user){} //returns User?
    public void deletePlayer(){} //probably needs input, ID
}
