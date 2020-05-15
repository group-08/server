package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;


    @WebAppConfiguration
    @SpringBootTest
    public class AuthServiceIntegrationTest {

        @Qualifier("userRepository")
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserService userService;

        @BeforeEach
        public void setup() {
            userRepository.deleteAll();
        }

        @AfterEach
        public void tearDown() {
            userRepository.deleteAll();
        }
    }
