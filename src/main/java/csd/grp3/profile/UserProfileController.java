package csd.grp3.profile;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import csd.grp3.user.UserService;

@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserService userService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserService userService) {
        this.userProfileService = userProfileService;
        this.userService = userService;
    }

    /**
     * Updates user ELO, using UserService to find User by username, then finding profile by User.
     * 
     * @param username - User's username (unique)
     * @param elo - new ELO
     */
    @PutMapping("/profile/{username}/elo")
    public void updateElo(@PathVariable String username, @RequestParam int elo) {
        // User user = userService.findByUsername(username);
        // Basically get user here, then call
        // UserProfile profile = userProfileService.getProfileByUser(user);
        // then update elo
        // do check if user and profile are present (not null) before updating

    }

    @PutMapping("/profile/{username}/display-name")
    public void updateDisplayName(@RequestParam String displayName) {
        userProfileService.modifyDisplayName(displayName);
    }

    @GetMapping("/history")
    public List<Tournament> getHistory() {
        return userProfileService.showHistory();
    }

    @GetMapping("/registered")
    public List<Tournament> getRegistered() {
        return userProfileService.showRegistered();
    }

    @PostMapping("/history")
    public void addToHistory(@RequestBody Tournament tournament) {
        userProfileService.addHistory(tournament);
    }

    @PostMapping("/registered")
    public void addToRegistered(@RequestBody Tournament tournament) {
        userProfileService.addRegistered(tournament);
    }

    @DeleteMapping("/registered")
    public void removeFromRegistered(@RequestBody Tournament tournament) {
        userProfileService.removeRegistered(tournament);
    }
}
