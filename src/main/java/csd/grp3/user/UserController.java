package csd.grp3.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.createNewUser(user.getUsername(), user.getPassword()) == null) {
            model.addAttribute("status", "User registration failed.");
            model.addAttribute("message", "error message");
            System.out.println("[User Registration]: Failed");
        } else {
            model.addAttribute("status", "User registered successfully.");
            System.out.println("[User Registration]: Successfully added: " + user.getUsername());
        }        
        return "result";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        model.addAttribute("message", "User Log In successfully!");
        return "result";
    }
    

    @PutMapping("/user/{username}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
}
