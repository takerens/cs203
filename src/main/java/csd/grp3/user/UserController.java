package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private User getUser() {
        return this.user;
    }
    // Till HERE

    public UserController(UserService userService) {
        this.userService = userService;
        this.user = null;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        if (getUser() != null) {
            return ResponseEntity.ok(getUser());    
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        User loggedIn = userService.login(user.getUsername(), user.getPassword());
        setUser(loggedIn);
        return ResponseEntity.status(HttpStatus.OK).body(loggedIn);
    }
}
