package ch.uzh.ifi.seal.soprafs20.User;

import game.Game;

import java.util.ArrayList;

public class User {
    Long id;
    String username;
    String password;
    String token;
    UserStatus status;
    ArrayList<Game> games;

    public void createUser(){}
    public void login(){}
    public void logout(){}
}
