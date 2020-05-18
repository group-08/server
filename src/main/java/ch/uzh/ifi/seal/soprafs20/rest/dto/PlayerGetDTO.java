package ch.uzh.ifi.seal.soprafs20.rest.dto;


import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.user.Colour;
import ch.uzh.ifi.seal.soprafs20.user.User;

import java.util.List;

public class PlayerGetDTO {

    private long id;

    private User user;

    private Colour colour;

    private List<Card> hand;

    private boolean exchangeCards;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserGetDTO getUser() {
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(this.user);
        return userGetDTO;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public boolean getExchangeCards() {
        return exchangeCards;
    }

    public void setExchangeCards(boolean exchangeCards) {
        this.exchangeCards = exchangeCards;
    }
}
