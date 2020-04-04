package ch.uzh.ifi.seal.soprafs20.User;

import game.Game;

import java.util.ArrayList;

public class Lobby {
    ArrayList<Game> games;
    ArrayList<User> users;

    public void createGame(){
        Game game = new Game();
    } //returns game?
    public void deleteGame(){}
    public void createPlayer(User user){} //returns User?
    public void deletePlayer(){} //probably needs input, ID
}
