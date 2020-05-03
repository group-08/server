package ch.uzh.ifi.seal.soprafs20.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ch.uzh.ifi.seal.soprafs20.user.Figure;

@Repository("figureRepository")
public interface FigureRepository extends JpaRepository<Figure, Long> {
}
