package csd.grp3.match;

import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor

@RestController
public class MatchController {
    private MatchService matchService;

    @PutMapping("/match/updateList")
    public List<Match> updateMatches(@RequestBody List<Match> matches) {
        List<Match> updatedList = new ArrayList<>();
        for (Match match : matches) {
            updatedList.add(matchService.updateMatch(match.getId(), match));
        }
        return updatedList;
    }
}
