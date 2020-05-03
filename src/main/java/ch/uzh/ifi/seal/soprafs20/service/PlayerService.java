package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.cards.Card;
import ch.uzh.ifi.seal.soprafs20.cards.Value;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.user.Figure;
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
    private BoardService boardService;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("gameRepository") GameRepository gameRepository,
                         @Qualifier("boardService") BoardService boardService){
        this.playerRepository=playerRepository;
        this.gameRepository = gameRepository;
        this.boardService = boardService;
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

    public boolean checkIfCanPlay(long gameId, Player player) {
        boolean canPlay = false;
        for (Card card : player.getHand()) {
            for (Figure figure : player.getFigures()) {
                if (card.getValue() == Value.SEVEN) {
                    if (boardService.getPossibleFieldsSeven(card, figure.getField(), 7) != null) {
                        canPlay = true;
                    }
                } else if (card.getValue() == Value.JACK) {
                    if (boardService.getPossibleFieldsJack(gameId, card, figure.getField()) != null) {
                        canPlay = true;
                    }
                } else {
                    if (boardService.getPossibleFields(gameId, card, figure.getField()) != null) {
                        canPlay = true;
                    }
                }
            }
        }
        return canPlay;
    }
}
