package csd.grp3.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import csd.grp3.tournament.TournamentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    public UserController(UserService userService, TournamentService tournamentService) {

        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    // @GetMapping("/rounds")
    // public String getMethodName(Model model) {
    //     Tournament t = new Tournament();
    //     t.setTitle("tounr");
    //     t.setId(3);
    //     model.addAttribute("currentRound", 2);
    //     model.addAttribute("tournament", t);
    //     return "round";
    // }

    @PostMapping("/register/{tournamentId}")
    public String registerForTournament(@PathVariable Long tournamentId, HttpSession session,
            RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            System.out.println("[Error]: User is not authenticated. Please log in first");
            return "redirect:/login?error=Please log in first";
        }

        User user = userService.getUser(username);

        tournamentService.registerUser(user, tournamentId);
        redirectAttributes.addFlashAttribute("message", "Successfully registered for the tournament!");
        return "redirect:/tournaments"; // Redirect after successful registration
    }


    @DeleteMapping("/withdraw/{tournamentId}")
    public String withdrawfromTournament(@PathVariable Long tournamentId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            System.out.println("[Error]: User is not authenticated. Please log in first");
            return "redirect:/login?error=Please log in first";
        }
    
        User user = userService.getUser(username);

        tournamentService.withdrawUser(user, tournamentId);
        redirectAttributes.addFlashAttribute("message", "Successfully withdrew from the tournament!");
        return "redirect:/tournaments"; // Redirect after successful withdrawal
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.createNewUser(user.getUsername(), user.getPassword()) == null) {
            model.addAttribute("errorMessage", "User registration failed.");
            return "signup";
        }
        model.addAttribute("message", "User registered successfully.");
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
            return "redirect:/index"; // Redirect on successful login
        }
        model.addAttribute("errorMessage", "Login Failed");
        return "login"; // Redirect on failure
    }
  
    // Doesn't run
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        String username = (String) session.getAttribute("username"); // debugging
        System.out.println("[User Logout]: " + username + " has logged out"); // debugging
        session.invalidate(); // Invalidates the session and removes all attributes
        return "login"; // Redirect to the login page
    }
}
