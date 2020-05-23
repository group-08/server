package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostLoginDTO;
import ch.uzh.ifi.seal.soprafs20.user.User;
import ch.uzh.ifi.seal.soprafs20.user.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    private UserPostLoginDTO userPostLoginDTO;

    @BeforeEach
    void setup() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("mail");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser);

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(testUser);
        Mockito.when(userRepository.findByToken(Mockito.anyString())).thenReturn(testUser);
    }

    @Test
    void loginTestCorrect() throws IllegalAccessException {


        UserPostLoginDTO userPostLoginDTO = new UserPostLoginDTO();
        userPostLoginDTO.setEmail("mail");
        userPostLoginDTO.setPassword("password");

        User theUser = authService.login(userPostLoginDTO);
        assert theUser != null;

        Assertions.assertEquals("mail", theUser.getEmail());

        Assertions.assertEquals(UserStatus.ONLINE, theUser.getStatus());


    }

    @Test
    void loginTestIncorrect() throws IllegalAccessException{


        UserPostLoginDTO userPostLoginDTOFalse = new UserPostLoginDTO();
        userPostLoginDTOFalse.setPassword("wrongPassword");
        userPostLoginDTOFalse.setEmail("mail");


        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            authService.login(userPostLoginDTOFalse) ;});
        String message = "wrong password";
        assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void logoutTest(){

        String userToken = "randomString";

        authService.logout(userToken);

        Assertions.assertEquals(UserStatus.OFFLINE, testUser.getStatus());
        Assertions.assertEquals(null, testUser.getToken());

    }
}