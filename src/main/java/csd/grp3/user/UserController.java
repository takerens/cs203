package csd.grp3.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import csd.grp3.tournament.TournamentService;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    public UserController(UserService userService, TournamentService tournamentService) {

        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    @PostMapping("/register/{tournamentId}")
    public String registerForTournament(@PathVariable Long tournamentId, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/tournaments"; // Redirect on failure
        }

        if (!tournamentService.tournamentExists(tournamentId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tournament not found.");
            return "redirect:/tournaments"; // Redirect on failure
        }

        tournamentService.registerPlayer(user, tournamentId);
        redirectAttributes.addFlashAttribute("message", "Successfully registered for the tournament!");
        return "redirect:/tournaments"; // Redirect after successful registration
    }

    @PostMapping("/withdraw/{tournamentId}")
    public String withdrawfromTournament(@PathVariable Long tournamentId, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/tournaments"; // Redirect on failure
        }

        if (!tournamentService.tournamentExists(tournamentId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tournament not found.");
            return "redirect:/tournaments"; // Redirect on failure
        }

        tournamentService.withdrawPlayer(user, tournamentId);
        redirectAttributes.addFlashAttribute("message", "Successfully withdrew from the tournament!");
        return "redirect:/tournaments"; // Redirect after successful withdrawal
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/signup")
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
    public String loginUser(@ModelAttribute User user, Model model) {
        model.addAttribute("message", "User Log In successfully!");
        return "redirect:/tournament";
    }
}
