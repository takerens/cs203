package csd.grp3.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolation; 
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // @Test
    // public void getUserDetails_Success() {
    //     //Arrange
    //     User user = new User("username", "password");
    //     when(userService.login(user.getUsername(), user.getPassword())).thenReturn(user);
    //     userController.loginUser(user);

    //     ResponseEntity<User> response = userController.getUserDetails();

    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(user, response.getBody());
    // }

    // @Test
    // public void getUserDetails_Fail_ReturnNotFound() {
    //     ResponseEntity<User> response = userController.getUserDetails();

    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    //     assertEquals(null, response.getBody());
    // }

    @Test
    public void registerUser_Success() throws MethodArgumentNotValidException {

        User user = new User("username", "password");

        when(userService.createNewUser(any(String.class), any(String.class))).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(user);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testRegisterUserWithNullUsername_ValidationError() {
        // Create a User object with a null username
        User mockUser = new User(null, "validPassword123");

        // Manually validate the object to simulate the behavior of @Valid
        Set<ConstraintViolation<User>> violations = validator.validate(mockUser);

        // Check that there is a validation error
        assertFalse(violations.isEmpty(), "Expected validation error for null username");
        assertEquals(1, violations.size());
        String violationMessage = violations.iterator().next().getMessage();
        assertEquals("Username should not be null", violationMessage);

        // Since we expect a validation error, we should not call the user service
        // In an actual request scenario, the service method would not be called due to validation
        verify(userService, never()).createNewUser(anyString(), anyString());
    }

    @Test
    public void testRegisterUserWithNullPassword_ValidationError() {
        // Create a User object with a null password
        User mockUser = new User("username", null);

        // Manually validate the object to simulate the behavior of @Valid
        Set<ConstraintViolation<User>> violations = validator.validate(mockUser);

        // Check that there is a validation error
        assertFalse(violations.isEmpty(), "Expected validation error for null password");
        assertEquals(1, violations.size());
        String violationMessage = violations.iterator().next().getMessage();
        assertEquals("Password should not be null", violationMessage);

        // Since we expect a validation error, we should not call the user service
        // In an actual request scenario, the service method would not be called due to validation
        verify(userService, never()).createNewUser(anyString(), anyString());
    }

    @Test
    public void testRegisterUserWithShortPassword_ValidationError() {
        // Create a User object with a short password
        User mockUser = new User("username", "1234567");

        // Manually validate the object to simulate the behavior of @Valid
        Set<ConstraintViolation<User>> violations = validator.validate(mockUser);

        // Check that there is a validation error
        assertFalse(violations.isEmpty(), "Expected validation error for invalid password length");
        assertEquals(1, violations.size());
        String violationMessage = violations.iterator().next().getMessage();
        assertEquals("Password should be at least 8 characters long", violationMessage);

        // Since we expect a validation error, we should not call the user service
        // In an actual request scenario, the service method would not be called due to validation
        verify(userService, never()).createNewUser(anyString(), anyString());
    }

    @Test
    public void testViewProfile_UserExists_ReturnOK() {
        // Arrange
        String username = "testuser";
        User mockUser = new User(username, "password123");
        
        // Mock the behavior of userService.findByUsername()
        when(userService.findByUsername(username)).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.viewProfile(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertEquals(mockUser, response.getBody(), "Expected user returned by the service");

        // Verify that userService.findByUsername() was called with the correct username
        verify(userService).findByUsername(username);
    }

    @Test
    public void testViewProfile_UserNotFound_ReturnNotFound() {
        // Arrange
        String username = "nonexistentuser";
        
        // Mock the userService to throw UsernameNotFoundException
        when(userService.findByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act and assert
        assertThrows(UsernameNotFoundException.class,()-> {
            userController.viewProfile(username);
        });

        // Verify that the service method was called
        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    public void testChangePassword_ValidPassword_ReturnOK() {
        // Arrange
        User user = new User("testUser", "Password123");
        User updatedUser = new User("testUser", "newPassword123"); // Mimic the updated user object
        
        // Mock the userService behavior
        when(userService.changePassword(user.getUsername(), user.getPassword())).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.changePassword(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());

        // Verify that the service method was called
        verify(userService, times(1)).changePassword(user.getUsername(), user.getPassword());
    }

    @Test
    public void testChangePassword_NullPassword_ValidationError() {
        // Arrange
        User updatedUser = new User("testUser", null); // Mimic the updated user object
        
        // Manually validate the object to simulate the behavior of @Valid
        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);

        // Check that there is a validation error
        assertFalse(violations.isEmpty(), "Expected validation error for invalid password length");
        assertEquals(1, violations.size());
        String violationMessage = violations.iterator().next().getMessage();
        assertEquals("Password should not be null", violationMessage);

        // Since we expect a validation error, we should not call the user service
        // In an actual request scenario, the service method would not be called due to validation
        verify(userService, never()).createNewUser(anyString(), anyString());
    }

    @Test
    public void testChangePassword_ShortPassword_ValidationError() {
        // Arrange
        User updatedUser = new User("testUser", "1234567"); // Mimic the updated user object
        
        // Manually validate the object to simulate the behavior of @Valid
        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);

        // Check that there is a validation error
        assertFalse(violations.isEmpty(), "Expected validation error for invalid password length");
        assertEquals(1, violations.size());
        String violationMessage = violations.iterator().next().getMessage();
        assertEquals("Password should be at least 8 characters long", violationMessage);

        // Since we expect a validation error, we should not call the user service
        // In an actual request scenario, the service method would not be called due to validation
        verify(userService, never()).createNewUser(anyString(), anyString());
    }

    
}
