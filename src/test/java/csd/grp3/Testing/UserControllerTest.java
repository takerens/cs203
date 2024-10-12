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
import csd.grp3.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser", "password123");
    }

    @Test
    public void testRegisterUser_Success() {
        when(userService.createNewUser(user.getUsername(), user.getPassword())).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    public void testLoginUser_Success() {
        when(userService.login(user.getUsername(), user.getPassword())).thenReturn(user);

        ResponseEntity<User> response = userController.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    public void testGetUserDetails_UserExists() {
        userController.setUser(user);

        ResponseEntity<User> response = userController.getUserDetails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    public void testGetUserDetails_UserNotFound() {
        userController.setUser(null);

        ResponseEntity<User> response = userController.getUserDetails();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testViewProfile_Success() {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response = userController.viewProfile(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    public void testChangePassword_Success() {
        when(userService.changePassword(user.getUsername(), user.getPassword())).thenReturn(user);

        ResponseEntity<User> response = userController.changePassword(user.getUsername(), user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }
}
