package csd.grp3.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class UserController {
    private UserService userService;

    // //TEMPORARY
    // private User user;

    // public void setUser(User user) {
    //     this.user = user;
    // }

    // User getUser() {
    //     return this.user;
    // }
    // // Till HERE

    public UserController(UserService userService) {
        this.userService = userService;
        // this.user = null; //TEMP
    }

    // @GetMapping("/user")
    // public ResponseEntity<User> getUserDetails() {
    //     if (getUser() != null) {
    //         return ResponseEntity.ok(getUser());
    //     }
    //     return ResponseEntity.notFound().build();
    // }
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.listUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        // setUser(loggedIn);
        return ResponseEntity.status(HttpStatus.OK).body(token);
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

    @DeleteMapping("/profile/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(username + " has been deleted");
    }
}