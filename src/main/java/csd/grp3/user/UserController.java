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
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String homePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUser(username);

        model.addAttribute("username", username);
        model.addAttribute("userRole", user.getUserRole());
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidates the session and removes all attributes
        return "redirect:/login"; // Redirect to the login page
    }
    
}
