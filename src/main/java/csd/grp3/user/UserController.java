package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csd.grp3.tournament.TournamentService;

import jakarta.validation.Valid;



@RestController
public class UserController {
    private UserService userService;

    private TournamentService tournamentService;

    public UserController(UserService userService, TournamentService tournamentService) {

        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody User user) {
        if (userService.createNewUser(user.getUsername(), user.getPassword()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@RequestBody User user) {
        if (userService.login(user.getUsername(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register/{tournamentId}")
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
