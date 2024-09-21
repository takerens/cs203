package csd.grp3.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;


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
            model.addAttribute("errorMessage", "User registration failed.");
            System.out.println("[User Registration]: Failed");
            return "register";
        }
        model.addAttribute("message", "User registered successfully.");
        System.out.println("[User Registration]: Successfully added: " + user.getUsername());
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpSession session) {
        session.setAttribute("username", user.getUsername());
        model.addAttribute("username", user.getUsername());
        if (user.getUsername().equals("user")) {
            model.addAttribute("userRole", "ROLE_USER");
        } else if (user.getUsername().equals("admin")) {
            model.addAttribute("userRole", "ROLE_ADMIN");
        }
        return "index";
    }

    @GetMapping("/index")
    public String homePage(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidates the session and removes all attributes
        return "redirect:/login"; // Redirect to the login page
    }
    
}
