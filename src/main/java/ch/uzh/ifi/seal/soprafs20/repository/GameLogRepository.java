package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.GameLog;

import ch.uzh.ifi.seal.soprafs20.User.User;
import ch.uzh.ifi.seal.soprafs20.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("gameLogRepository")
public interface GameLogRepository extends JpaRepository<GameLog, Long> {

}
