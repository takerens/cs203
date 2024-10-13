package csd.grp3.user;


import csd.grp3.user.User;
import csd.grp3.tournament.*;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testUser", "password");        
    }

    @Test
    public void getUserDetails_Success() {
        
        ResponseEntity<User> response = userController.getUserDetails();

        assertEquals(HttpStatus.OK, response);
        assertNotNull(response.getBody());
    }

    @Test
    public void registerUser_Success() throws MethodArgumentNotValidException {

        when(userService.createNewUser(any(String.class), any(String.class))).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(user);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void registerUser_UsernameIsNull_Fail() {
        User badUser = new User(null, "password");

        try {
            ResponseEntity<User> response = userController.registerUser(badUser);
        } catch (MethodArgumentNotValidException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.get)
        }
        
        

    }
    
}
