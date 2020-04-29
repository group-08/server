package ch.uzh.ifi.seal.soprafs20.repository;


import ch.uzh.ifi.seal.soprafs20.User.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
