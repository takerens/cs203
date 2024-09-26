package csd.grp3.profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csd.grp3.tournament.Tournament;

@RestController
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PutMapping("/elo")
    public void updateElo(@RequestParam int elo) {
        userProfileService.modifyElo(elo);
    }

    @PutMapping("/display-name")
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
