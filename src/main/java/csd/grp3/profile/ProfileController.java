package csd.grp3.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import csd.grp3.user.UserService;


@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @PutMapping("/profile/{username}/elo")
    public ResponseEntity<HttpStatus> updateElo(@PathVariable String username, @RequestParam int elo) {
        User user = userService.findByUsername(username);
        profileService.modifyElo(user, elo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/profile/{username}/display-name")
    public ResponseEntity<HttpStatus> updateDisplayName(@PathVariable String username, @RequestParam String displayName) {
        User user = userService.findByUsername(username);
        profileService.modifyDisplayName(user, displayName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Tournament>> getHistory(@PathVariable String username) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        return new ResponseEntity<>(profile.getHistory(), HttpStatus.OK);     // how to handle exception??
    }

    @GetMapping("/history")
    public ResponseEntity<List<Tournament>> getRegistered(@PathVariable String username) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        return new ResponseEntity<>(profile.getRegistered(), HttpStatus.OK);     // how to handle exception??
    }

    @PostMapping("/history")
    public ResponseEntity<HttpStatus> addToHistory(@PathVariable String username, @RequestParam Tournament tournament) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        profile.addTournamentToHistory(tournament);
        return new ResponseEntity<>(HttpStatus.OK);     // how to handle exception??
    }

    @PostMapping("/registered")
    public ResponseEntity<HttpStatus> addToRegistered(@PathVariable String username, @RequestParam Tournament tournament) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        profile.addTournamentToRegistered(tournament);
        return new ResponseEntity<>(HttpStatus.OK);     // how to handle exception??
    }

    @DeleteMapping("/registered")
    public ResponseEntity<HttpStatus> removeFromRegistered(@PathVariable String username, @RequestParam Tournament tournament) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        profile.removeTournamentFromRegistered(tournament);
        return new ResponseEntity<>(HttpStatus.OK);     // how to handle exception??
    }
}
