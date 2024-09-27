package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import csd.grp3.tournament.TournamentService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    private User user;

    private void setUser(User user) {
        this.user = user;
    }

    private User getUser() {
        return this.user;
    }

    public UserController(UserService userService, TournamentService tournamentService) {
        this.userService = userService;
        this.tournamentService = tournamentService;
        this.user = null;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        if (getUser() != null) {
            return ResponseEntity.ok(getUser());    
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        if (userService.login(user.getUsername(), user.getPassword())) {
            setUser(userService.findByUsername(user.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // null is needed
    }

    @PostMapping("/register/{tournamentId}")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<HttpStatus> registerForTournament(@PathVariable Long tournamentId, @RequestBody User user) {
        if (user == null || !tournamentService.tournamentExists(tournamentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        tournamentService.registerPlayer(user, tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/withdraw/{tournamentId}")
    public ResponseEntity<HttpStatus> withdrawfromTournament(@PathVariable Long tournamentId, @RequestBody User user) {
        if (user == null || !tournamentService.tournamentExists(tournamentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        tournamentService.withdrawPlayer(user, tournamentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
