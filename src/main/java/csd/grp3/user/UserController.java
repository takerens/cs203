package csd.grp3.user;

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

    @PostMapping("/verify")
    public String loginUser(@ModelAttribute User user, Model model, HttpSession session) {
        if (userService.login(user.getUsername(), user.getPassword())) {
            session.setAttribute("username", user.getUsername());
            System.out.println("[User Login]: " + user.getUsername() + " logged in successfully");
            return "redirect:/index"; // Redirect on successful login
        }
        System.out.println("[User Login]: " + user.getUsername() + " failed to log in");
        return "redirect:/login?error=Login Failed"; // Redirect on failure
    }

    @GetMapping("/index")
    public String homePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            System.out.println("[Error]: User is not authenticated. Please log in first");
            return "redirect:/login?error=Please log in first";
        }
        User user = userService.getUser(username);
        model.addAttribute("username", username);
        model.addAttribute("userRole", user.getUserRole());
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        String username = (String) session.getAttribute("username"); // debuggy
        System.out.println("[User Logout]: " + username + " has logged out"); // debugging
        session.invalidate(); // Invalidates the session and removes all attributes
        return "redirect:/login"; // Redirect to the login page
    }
}
