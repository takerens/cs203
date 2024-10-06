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
import java.util.Optional;


@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @PutMapping("/profile/{username}/elo")
    public ResponseEntity<HttpStatus> updateElo(@PathVariable String username, @RequestParam int elo) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        profileService.modifyElo(user, elo);                        // might throw ProfileNotFoundException - handle in controller advice
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/profile/{username}/display-name")
    public ResponseEntity<HttpStatus> updateDisplayName(@PathVariable String username, @RequestParam String displayName) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        profileService.modifyDisplayName(user, displayName);        // might throw ProfileNotFoundException - handle in controller advice
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile/{username}/history")
    public ResponseEntity<List<Tournament>> getHistory(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        Profile profile = profileService.getProfileByUser(user);    // might throw ProfileNotFoundException - handle in controller advice
        return new ResponseEntity<>(profile.getHistory(), HttpStatus.OK);
    }

    @GetMapping("/profile/{username}/registered")
    public ResponseEntity<List<Tournament>> getRegistered(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));           // might throw ProfileNotFoundException - handle in controller advice
        Profile profile = profileService.getProfileByUser(user);
        return new ResponseEntity<>(profile.getRegistered(), HttpStatus.OK);
    }

    @PostMapping("/profile/{username}/history")
    public ResponseEntity<HttpStatus> addToHistory(@PathVariable String username, @RequestParam Long id) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        profileService.addHistory(user, id);        // might throw ProfileNotFoundException or TournamentNotFoundException - handle in controller advice
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/profile/{username}/registered")
    public ResponseEntity<String> addToRegistered(@PathVariable String username, @RequestParam Long id) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        profileService.addRegistered(user, id);     // might throw ProfileNotFoundException or TournamentNotFoundException - handle in controller advice
        return new ResponseEntity<>("tournament added", HttpStatus.OK);
    }

    @DeleteMapping("/profile/{username}/registered")
    public ResponseEntity<HttpStatus> removeFromRegistered(@PathVariable String username, @RequestParam Long id) {
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new ProfileNotFoundException("User not found with username: " + username));
        profileService.removeRegistered(user, id);  // might throw ProfileNotFoundException or TournamentNotFoundException - handle in controller advice
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
