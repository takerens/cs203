package csd.grp3.user;

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

        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername(username);
        });
    }

    @Test
    void changePassword_ValidUsernameAndPassword_ReturnUser() {
        String username = "username";
        String password = "oldpassword";
        User user = new User(username, password);
        String newPassword = "newpassword";
        User updated = new User(username, newPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        User result = userService.changePassword(username, newPassword);

        assertEquals(username, result.getUsername());
        assertEquals(newPassword, result.getPassword());
    }

    @Test
    void changePassword_InvalidUsername_ThrowsUsernameNotFoundException() {
        String username = "username";
        String newPassword = "newpassword";

        assertThrows(UserNotFoundException.class, ()-> {
            userService.changePassword(username, newPassword);
        });
    }
    
}