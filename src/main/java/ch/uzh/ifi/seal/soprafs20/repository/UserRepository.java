package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    User findByToken(String token);

    Boolean existsUserByUsername(String username);

    Boolean existsByToken(String token);
}
