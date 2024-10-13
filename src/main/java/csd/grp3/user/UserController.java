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
    private User user;

    private void setUser(User user) {
        this.user = user;
    }

    User getUser() {
        return this.user;
    }
    // Till HERE

    public UserController(UserService userService) {
        this.userService = userService;
        this.user = null; //TEMP
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        if (getUser() != null) {
            return ResponseEntity.ok(getUser());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws MethodArgumentNotValidException {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        User loggedIn = userService.login(user.getUsername(), user.getPassword());
        setUser(loggedIn);
        return ResponseEntity.status(HttpStatus.OK).body(loggedIn);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> viewProfile(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(username));
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<User> changePassword(@Valid @RequestBody User user) {
        User updatedUser = userService.changePassword(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}