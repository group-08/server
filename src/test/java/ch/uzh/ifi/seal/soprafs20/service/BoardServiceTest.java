package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;

import ch.uzh.ifi.seal.soprafs20.board.Board;
import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.NormalCard;
import ch.uzh.ifi.seal.soprafs20.cards.Suit;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.field.Field;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.user.*;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.seal.soprafs20.user.*;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private BoardService boardService;

    @InjectMocks
    private GameService gameService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPossibleFields_Test() {

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@Name.tld");
        testUser.setUsername("testUsername");

        Player testPlayer = new Player();
        testPlayer.setUser(testUser);

        Game testGame = new Game(testUser, "testGame");

        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(testGame));

        Figure figure = new Figure();
        figure.setPlayer(testPlayer);

        long id = 0;
        for(Field fieldSetId : testGame.getBoard().getFields()){
            fieldSetId.setId(id);
            id++;
        }

        Field field = testGame.getBoard().getField(1);

        field.setOccupant(figure);

        Card cardFIVE = new NormalCard(Suit.CLUBS, Value.FIVE);
        Card cardTEN = new NormalCard(Suit.CLUBS, Value.TEN);

        List<Field> possibleFieldsFIVE = boardService.getPossibleFields(1L, cardFIVE, field);
        List<Field> possibleFieldsTEN = boardService.getPossibleFields(1L, cardTEN, field);

        Assert.assertEquals(field.getId()+5, possibleFieldsFIVE.get(0).getId());
        Assert.assertEquals(field.getId()+10, possibleFieldsTEN.get(0).getId());
    }
}