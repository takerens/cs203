package csd.grp3.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;

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
}
