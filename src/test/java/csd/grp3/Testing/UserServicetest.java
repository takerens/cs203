package csd.grp3.Testing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

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
        String username = "existingUser";
        String password = "Password123";
        userRepository.save(new User(username, password));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password)));

        assertThrows(BadCredentialsException.class, ()->userService.createNewUser(username, password));
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
    @Test
    void login_Successful_ReturnUser() {
        String username = "testUser";
        String rawPassword = "Password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
    
    }
    
    // Test for login with non-existent username (should return false)
    @Test
    void testLogin_usernameNotFound() {
        // String username = "unknownUser";
        // String password = "Password123";
    
        // when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    
        // boolean result = userService.login(username, password);
    
        // assertFalse(result, "Login should return false for non-existent username.");
    }
    
    // Test for login with incorrect password (should return false)
    @Test
    void testLogin_invalidPassword() {
        // String username = "testUser";
        // String rawPassword = "Password123";
        // String wrongPassword = "WrongPassword";
        // String encodedPassword = encoder.encode(rawPassword);
    
        // User user = new User(username, encodedPassword);
        // when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    
        // boolean result = userService.login(username, wrongPassword);
    
        // assertFalse(result, "Login should return false for incorrect password.");
    }


    // Test for finding all users
    @Test
    void testFindAll() {
        List<User> userList = List.of(
            new User("user1", "encodedPassword1"),
            new User("user2", "encodedPassword2")
        );
        when(userRepository.findAll()).thenReturn(userList);

        // List<User> result = userService.findAll();
        // assertEquals(2, result.size(), "Should return the correct number of users.");
    }

    // Test for finding a user by username successfully
    @Test
    void testFindByUsername_successful() {
        String username = "testUser";
        User user = new User(username, "encodedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findByUsername(username);
        assertNotNull(result);
        assertEquals(user, result);
    }

    // Test for finding a user by username when user is not found
    @Test
    void testFindByUsername_userNotFound() {
        String username = "unknownUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Exception exception = assertThrows(RuntimeException.class, () -> {
        //     userService.getUser(username);
        // });

        // assertEquals("User not found", exception.getMessage());
    }
}