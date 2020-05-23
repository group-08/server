package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostLoginDTO;
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

        // Actually login the user
        public User login(UserPostLoginDTO loginPostUser) throws IllegalAccessException{
            // Get the user trying to login
            User loginUser = this.userRepository.findByEmail(loginPostUser.getEmail());

            if (loginUser == null) {
                throw new IllegalAccessException();
            }

            // Check the password
            if (!loginUser.checkPassword(loginPostUser.getPassword())) {
                throw new IllegalAccessException();
            }

            // Update a token and set user online
            loginUser.setToken(UUID.randomUUID().toString());
            loginUser.setStatus(UserStatus.ONLINE);
            loginUser = this.userRepository.saveAndFlush(loginUser);

            return loginUser;
        }

        // Proper logout, so the user goes offline
        public void logout(String userToken) {
            try {
                User logoutUser = this.userRepository.findByToken(userToken);
                logoutUser.setStatus(UserStatus.OFFLINE);
                logoutUser.setToken(null);
                this.userRepository.saveAndFlush(logoutUser);
            }
            catch (NullPointerException e){

            }

        }

    }

