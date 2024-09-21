package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import csd.grp3.tournament.TournamentService;
import org.springframework.security.core.Authentication;

@Controller
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register/{tournamentId}")
    public ResponseEntity<Void> registerForTournament(@PathVariable Long tournamentId, Authentication authentication) {
        String username = authentication.getName(); // Get the currently authenticated user's name
        User user = userService.findByUsername(username); // Find the user by username

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tournamentService.registerPlayer(user, tournamentId); // Register the user for the tournament
        return new ResponseEntity<>(HttpStatus.OK);
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
    public String loginUser(@ModelAttribute User user, Model model) {
        model.addAttribute("message", "User Log In successfully!");
        return "index";
    }

    
}
