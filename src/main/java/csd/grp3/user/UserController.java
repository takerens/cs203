package csd.grp3.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

   @GetMapping("/users")
   public List<User> getUsers() {
       return userService.findAll();
   }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        System.out.println(newUser.getUsername() + ":" + newUser.getPassword());
        if (userService.createNewUser(newUser.getUsername(), newUser.getPassword()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //TODO SHOULD NOT BE NOTFOUND, ADD PROPER EXCEPTION
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
