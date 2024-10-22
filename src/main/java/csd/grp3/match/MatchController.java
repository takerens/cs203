package csd.grp3.match;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MatchController {
    @Autowired
    private MatchService matchService;

    @PutMapping("/match/updateList")
    public List<Match> updateMatches(@Valid @RequestBody List<Match> matches) {
        List<Match> updatedList = new ArrayList<>();
        for (Match match : matches) {
            System.out.println("Attempting to update Match with ID: " + match.getId());
            updatedList.add(matchService.updateMatch(match.getId(), match));
            System.out.println("Successfully updated Match with ID: " + match.getId());
        }
        return updatedList;
    }
}
