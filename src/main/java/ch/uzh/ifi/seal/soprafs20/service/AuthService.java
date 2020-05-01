package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


    /**
     * Login Service
     * This class is the "worker" and responsible for all functionality related to the login
     */
    @Service
    @Transactional
    public class AuthService {

        private final UserRepository userRepository;

        @Autowired
        public AuthService(@Qualifier("userRepository") UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        // Check if username and password matches
        public Boolean checkPassword(String username, String password) {
            if (this.userRepository.existsUserByUsername(username)) {
                User user = this.userRepository.findByUsername(username);
                return user.checkPassword(password);
            }

            return false;
        }

        // Actually login the user
        public User login(User user) {
            User loginUser = this.userRepository.findByUsername(user.getUsername());
            loginUser.setToken(UUID.randomUUID().toString());
            loginUser.setStatus(UserStatus.ONLINE);
            return loginUser;
        }

        // Proper logout, so the user goes offline
        public void logout(String userToken) {
            try {
                User logoutUser = this.userRepository.findByToken(userToken);
                logoutUser.setStatus(UserStatus.OFFLINE);
                logoutUser.setToken(null);
            }
            catch (NullPointerException e){

            }
        }

        public User getUserByUsername(String username) {
            return this.userRepository.findByUsername(username);
        }

        public Boolean checkToken(String userToken) {
            return this.userRepository.existsByToken(userToken);
        }
    }

