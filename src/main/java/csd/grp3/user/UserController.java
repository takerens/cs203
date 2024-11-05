package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private UserService userService;
    //TEMPORARY
<<<<<<< HEAD
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    User getUser() {
        return this.user;
    }
    // Till HERE
=======
    private User currentUser; // currently logged-in user
>>>>>>> frontendcopyformerging

    public UserController(UserService userService) {
        this.userService = userService;
        this.currentUser = null; //TEMP - Initialize with no user
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);    
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws MethodArgumentNotValidException {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody User user) {
        currentUser  = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> viewProfile(@PathVariable String username) {
        User foundUser = userService.findByUsername(username);
        return ResponseEntity.ok(foundUser);
    }

    @PutMapping("/user")
    public ResponseEntity<User> changePassword(@Valid @RequestBody User user) {
        User updatedUser = userService.changePassword(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> deleteUser(@Valid @RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/profile/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(username + " has been deleted");
    }
}