package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import csd.grp3.tournament.TournamentService;

import jakarta.validation.Valid;



@RestController
public class UserController {
    private UserService userService;

    // private TournamentService tournamentService;

    public UserController(UserService userService) { // , TournamentService tournamentService

        this.userService = userService;
        // this.tournamentService = tournamentService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        // User createdUser = userService.createNewUser(user.getUsername(), user.getPassword()); // use this w/ exception handling
        User createdUser = userService.createNewUser(user.getUsername(), user.getPassword());
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody User user) {
        if (userService.login(user.getUsername(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // @PostMapping("/register/{tournamentId}")
    // public ResponseEntity<HttpStatus> registerForTournament(@PathVariable Long tournamentId, @RequestBody User user) {
    //     if (user == null || !tournamentService.tournamentExists(tournamentId)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }

    //     tournamentService.withdrawPlayer(user, tournamentId);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    // @PostMapping("/withdraw/{tournamentId}")
    // public ResponseEntity<HttpStatus> withdrawfromTournament(@PathVariable Long tournamentId, @RequestBody User user) {
    //     if (user == null || !tournamentService.tournamentExists(tournamentId)) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }

    //     tournamentService.withdrawPlayer(user, tournamentId);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }
}
