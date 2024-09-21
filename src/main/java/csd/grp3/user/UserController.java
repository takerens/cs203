package csd.grp3.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @GetMapping("/users")
//    public List<User> getUsers() {
//        return user.findAll();
//    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User newUser) {
        System.out.println(newUser.getUsername() + ":" + newUser.getPassword());
        userService.createNewUser(newUser.getUsername(), newUser.getPassword());
        return newUser;
    }
    
}
