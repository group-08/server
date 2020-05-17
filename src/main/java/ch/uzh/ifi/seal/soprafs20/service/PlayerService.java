package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.cards.Card;

import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlayerService {

    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private BoardService boardService;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("boardRepository") BoardRepository boardRepository,
                         @Qualifier("gameRepository") GameRepository gameRepository){
        this.playerRepository=playerRepository;
        this.gameRepository = gameRepository;
        this.boardService = new BoardService(boardRepository, gameRepository);
    }

    public void addGiftedCard(long gameId, long playerId, Card card){

        Game actualGame = gameRepository.findById(gameId).orElse(null);
        Player actualPlayer = playerRepository.findById(playerId).orElse(null);
        assert actualGame != null;
        assert actualPlayer != null;

        List<Player> players = actualGame.getPlayers();
        int indexOfActualPlayer = players.indexOf(actualPlayer);
        Player partner = players.get((indexOfActualPlayer+2)%4);

        List<Card> hand = partner.getHand();
        hand.add(card);
        partner.setHand(hand);
        playerRepository.saveAndFlush(partner);

    }

    public void removeCardFromHand(List<Card> hand, Card card){
        for(Card cardInHand : hand){
            if(card.equals(cardInHand)){
                hand.remove(cardInHand);
                return;
            }
        }
    }

    public void removeFromHand(Player actualPlayer, Card card){
        List<Card> hand = actualPlayer.getHand();
        removeCardFromHand(hand,card);
        actualPlayer.setHand(hand);
        playerRepository.saveAndFlush(actualPlayer);
    }

    public Player findById(long id){
        return playerRepository.findById(id).orElse(null);
    }

    public void exchange(long gameId, long playerId, Card card){
        Player player = playerRepository.findById(playerId).orElse(null);
        assert player != null;
        removeFromHand(player,card);
        addGiftedCard(gameId,playerId,card);

    }

    public void removeAllFromHand(Player player) {
        if (player.getHand().isEmpty()) {
            return;
        }
        List<Card> playersHand = new ArrayList<>(player.getHand());
        for (Card card : playersHand) {
            removeCardFromHand(player.getHand(), card);
        }
    }

    public boolean checkIfCanPlay(Game game, long playerId) {
        Player player = playerRepository.getOne(playerId);
        if (player.getHand().isEmpty()) {
            return false;
        }
        for (Card card : player.getHand()) {
            for (Figure figure : player.getFigures()) {
//                if (card.getValue() == Value.SEVEN) {
//                    for (int i=1; i <= 7; i++) {
//                        if (!(boardService.getPossibleFieldsSeven(card, figure.getField(), i).isEmpty())) {
//                            return true;
//                        }
//                    }
//                } else
                if (card.getValue() == Value.JACK) {
                    if (!(boardService.getPossibleFieldsJack(game, card, figure.getField()).isEmpty())) {
                        return true;
                    }
                } else if (card.getValue() == Value.JOKER){
                    return true;
                } else {
                    if (!(boardService.getPossibleFields(game, card, figure.getField()).isEmpty())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
