package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import csd.grp3.tournament.TournamentService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;



@RestController
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    public UserController(UserService userService, TournamentService tournamentService) {

        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUser(HttpSession session) {
        // String username = (String) session.getAttribute("username");
        // System.out.println(username);
        User user = userService.findByUsername("User");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        // User createdUser = userService.createNewUser(user.getUsername(), user.getPassword()); // use this w/ exception handling
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user, HttpSession session) {
        if (userService.login(user.getUsername(), user.getPassword())) {
            // session.setAttribute("username", user.getUsername());
            System.out.println(session.getAttribute("username"));
            return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // null is needed
    }

    @PostMapping("/register/{tournamentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<HttpStatus> registerForTournament(@PathVariable Long tournamentId, @RequestBody User user) {
        if (user == null || !tournamentService.tournamentExists(tournamentId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tournamentService.withdrawPlayer(user, tournamentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw/{tournamentId}")
    public ResponseEntity<HttpStatus> withdrawfromTournament(@PathVariable Long tournamentId, @RequestBody User user) {
        if (user == null || !tournamentService.tournamentExists(tournamentId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tournamentService.withdrawPlayer(user, tournamentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
