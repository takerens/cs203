package csd.grp3.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {
    private UserRepository users;

    public UserController(UserRepository users){
        this.users = users;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.findAll();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User newUser) {
        System.out.println(newUser.getUsername() + ":" + newUser.getPassword());
        users.save(newUser);
        return newUser;
    }
    
}
