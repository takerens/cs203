package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class UserController {
    private UserService userService;
    private User currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
        this.currentUser = null; // TEMP
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        return currentUser == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(currentUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws MethodArgumentNotValidException {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        this.currentUser = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(this.currentUser);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> viewProfile(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(username));
    }

    @PutMapping("/user")
    public ResponseEntity<User> changePassword(@Valid @RequestBody User user) {
        User updatedUser = userService.changePassword(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/profile/{username}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
