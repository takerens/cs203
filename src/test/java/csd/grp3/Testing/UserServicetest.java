package csd.grp3.Testing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.grp3.user.UserServiceImpl;
import csd.grp3.user.UserRepository;
import csd.grp3.user.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserServicetest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for creating a new user with valid credentials
    @Test
    void createNewUser_ValidUsernamePassword_ReturnUser() {
        //arrange
        String username = "testUser";
        String password = "Password";
        String encoded = encoder.encode(password);
        User user = new User(username, encoded);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        //act
        User newUser = userService.createNewUser(username, password);

        //assert
        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());
        verify(userRepository).findByUsername(newUser.getUsername());
        verify(userRepository).save(user);
    }

    // Test for creating a new user with an existing username
    @Test
    void createNewUser_ExistingUsername_ThrowBadCredentialsException() {
        //arrange
        String username = "existingUser";
        String password = "Password123";
        User user = new User(username,password);
        userRepository.save(user);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        //act and assert
        assertThrows(BadCredentialsException.class, ()->userService.createNewUser(username, password));
    }

    // Test for finding a user by username successfully
    @Test
    void findByUsername_Successful_ReturnUser() {
        String username = "testUser";
        User user = new User(username, "encodedPassword");
        userRepository.save(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(user, result);
    }

    // Test for finding a user by username when user is not found
    @Test
    void findByUsername_UserNotFound_ThrowUsernameNotFoundException() {
        String username = "unknownUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByUsername(username);
        });
    }

    @Test
    void changePassword_ValidUsernameAndPassword_ReturnUser() {
        String username = "username";
        String password = "oldpassword";
        User user = new User(username, password);
        userRepository.save(user);
        String newPassword = "newpassword";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user = userService.changePassword(username, newPassword);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    void changePassword_InvalidUsername_ThrowsUsernameNotFoundException() {
        String username = "username";
        String newPassword = "newpassword";

        assertThrows(UsernameNotFoundException.class, ()-> {
            userService.changePassword(username, newPassword);
        });
    }
    // // Test for creating a new user with an invalid password
    // @Test
    // void createNewUser_PasswordTooShort() {
    //     String username = "testUser";
    //     String shortPassword = "Pwd1";

    //     when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    //     User newUser = userService.createNewUser(username, shortPassword);
    //     assertNull(newUser, "Should return null when password doesn't meet the requirements.");
    // }
    // @Test
    // void login_Successful_ReturnUser() {
    //     String username = "username";
    //     String password = "password";
    //     // userService.createNewUser(username, password);
    //     String encoded = encoder.encode(password);
    //     User user = new User(username, encoded);
    //     userRepository.save(user);

    //     when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
    //     when(encoder.matches(any(String.class), any(String.class))).thenReturn(true);
    //     // when(encoder.matches(any(String.class), any(String.class))).thenReturn(true);

    //     User loggedIn = userService.login(username, password);

    //     assertNotNull(loggedIn);
    //     verify(userRepository).findByUsername(username);
    //     verify(encoder).matches(password, loggedIn.getPassword());
    // }
    

    // Test for login with non-existent username (should return false)
    // @Test
    // void testLogin_usernameNotFound() {
        // String username = "unknownUser";
        // String password = "Password123";
    
        // when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    
        // boolean result = userService.login(username, password);
    
        // assertFalse(result, "Login should return false for non-existent username.");
    // }
    
    // Test for login with incorrect password (should return false)
    // @Test
    // void testLogin_invalidPassword() {
        // String username = "testUser";
        // String rawPassword = "Password123";
        // String wrongPassword = "WrongPassword";
        // String encodedPassword = encoder.encode(rawPassword);
    
        // User user = new User(username, encodedPassword);
        // when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    
        // boolean result = userService.login(username, wrongPassword);
    
        // assertFalse(result, "Login should return false for incorrect password.");
    // }
}