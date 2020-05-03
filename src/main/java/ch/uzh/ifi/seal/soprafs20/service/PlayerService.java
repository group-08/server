package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PlayerService {

    private PlayerRepository playerRepository;
    private GameRepository gameRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("gameRepository") GameRepository gameRepository){
        this.playerRepository=playerRepository;
        this.gameRepository = gameRepository;
    }

    public void addGiftedCard(long gameId, long playerId, Card card){

        Game actualGame = gameRepository.findById(gameId).get();
        Player actualPlayer = playerRepository.findById(playerId).get();

        List<Player> players = actualGame.getPlayers();
        int indexOfActualPlayer = players.indexOf(actualPlayer);
        Player partner = players.get(indexOfActualPlayer+2);

        List<Card> hand = partner.getHand();
        hand.add(card);
        partner.setHand(hand);
        playerRepository.saveAndFlush(partner);

    }

    public void removeFromHand(long playerId, Card card){

        Player actualPlayer = playerRepository.findById(playerId).get();

        List<Card> hand = actualPlayer.getHand();
        hand.remove(card);
        actualPlayer.setHand(hand);
        playerRepository.saveAndFlush(actualPlayer);
    }

    public void exchange(long gameId, long playerId, Card card){
        addGiftedCard(gameId,playerId,card);
        removeFromHand(playerId,card);
    }

}
