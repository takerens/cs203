package csd.grp3.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import csd.grp3.user.UserService;


@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @PutMapping("/profile/{username}/elo")
    public void updateElo(@PathVariable String username, @RequestParam int elo) {
        User user = userService.findByUsername(username);
        profileService.modifyElo(user, elo);
    }

    @PutMapping("/profile/{username}/display-name")
    public void updateDisplayName(@PathVariable String username, @RequestParam String displayName) {
        User user = userService.findByUsername(username);
        profileService.modifyDisplayName(user, displayName);
    }

    @GetMapping("/history")
    public List<Tournament> getHistory(@PathVariable String username) {
        User user = userService.findByUsername(username);
        Profile profile = profileService.getProfileByUser(user);
        return profile.getHistory();
    }



    // @GetMapping("/history")
    // public List<Tournament> getHistory() {
    // return userProfileService.showHistory();
    // }

    // @GetMapping("/registered")
    // public List<Tournament> getRegistered() {
    // return userProfileService.showRegistered();
    // }

    // @PostMapping("/history")
    // public void addToHistory(@RequestBody Tournament tournament) {
    // userProfileService.addHistory(tournament);
    // }

    // @PostMapping("/registered")
    // public void addToRegistered(@RequestBody Tournament tournament) {
    // userProfileService.addRegistered(tournament);
    // }

    // @DeleteMapping("/registered")
    // public void removeFromRegistered(@RequestBody Tournament tournament) {
    // userProfileService.removeRegistered(tournament);
    // }
}
