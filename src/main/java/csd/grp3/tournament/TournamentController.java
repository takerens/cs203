package csd.grp3.tournament;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csd.grp3.match.Match;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    // @Autowired
    // private TournamentRepository tournamentRepo;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournamentList = tournamentService.listTournaments();
        return ResponseEntity.status(HttpStatus.OK).body(tournamentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournamentData = tournamentService.getTournament(id);
        return new ResponseEntity<>(tournamentData, HttpStatus.OK);
    }

    @GetMapping("/{id}/rounds")
    public ResponseEntity<List<Round>> getRoundData(@PathVariable Long id) {
        Tournament tournamentData = tournamentService.getTournament(id);
        if (!tournamentData.isOver()) {
            tournamentService.addRound(id);
            tournamentData = tournamentService.getTournament(id); // get updated tournament info
        } else { // tournament is over
            if (!tournamentData.isCalculated()) {
                Round last = tournamentData.getRounds().get(tournamentData.getTotalRounds() - 1);
                tournamentService.updateMatchResults(last);
                tournamentService.updateResults(last);
                tournamentService.endTournament(id);
            }
        }
        return new ResponseEntity<List<Round>>(tournamentData.getRounds(), HttpStatus.OK);
    }

    // @GetMapping("/tournaments/{title}")
    // public ResponseEntity<Tournament> getTournamentByTitle(@PathVariable String title) {
    //     Tournament tournamentData = tournamentRepo.findByTitle(title);
        
    //     if (tournamentData != null) {
    //         return new ResponseEntity<>(tournamentData, HttpStatus.OK);
    //     }

    //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addTournament(@RequestBody Tournament tournament) {
        tournamentService.addTournament(tournament);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateTournamentById(@PathVariable Long id, @RequestBody Tournament newTournamentData) {
        // updatedTournamentData.setMatches(newTournamentData.getMatches());
        // updatedTournamentData.setParticipants(newTournamentData.getParticipants());
        // updatedTournamentData.setWaitingList(newTournamentData.getWaitingList());
        tournamentService.updateTournament(id, newTournamentData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @PostMapping("/tournaments/title/{title}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<Tournament> updateTournamentByTitle(@PathVariable String title, @RequestBody Tournament newTournamentData) {
    //     Tournament oldTournamentData = tournamentRepo.findByTitle(title);

    //     if (oldTournamentData != null) {
    //         Tournament updatedTournamentData = oldTournamentData;
    //         updatedTournamentData.setTitle(newTournamentData.getTitle());
    //         updatedTournamentData.setDate(newTournamentData.getDate());
    //         // updatedTournamentData.setMatches(newTournamentData.getMatches());
    //         updatedTournamentData.setMaxElo(newTournamentData.getMaxElo());
    //         // updatedTournamentData.setParticipants(newTournamentData.getParticipants());
    //         updatedTournamentData.setMinElo(newTournamentData.getMinElo());
    //         updatedTournamentData.setSize(newTournamentData.getSize());
    //         updatedTournamentData.setWaitingList(newTournamentData.getWaitingList());

    //         Tournament tournamentObj = tournamentRepo.save(updatedTournamentData);
    //         return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    //     }

    //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @DeleteMapping("/tournaments/{title}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<HttpStatus> deleteTournamentByTitle(@PathVariable String title) {
    //     tournamentRepo.deleteByTitle(title);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    @DeleteMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody User user, @PathVariable Long id) {
        tournamentService.withdrawUser(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user, @PathVariable Long id) {
        tournamentService.registerUser(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO
    @GetMapping("/{id}/standings")
    public ResponseEntity<List<User>> getStandings(@PathVariable Long id) {
        List<User> users = tournamentService.getSortedUsers(id);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping("/byElo/{elo}")
    public ResponseEntity<List<Tournament>> getTournamentByElo(@PathVariable int elo) {
        List<Tournament> t = tournamentService.getUserEligibleTournament(elo);
        return new ResponseEntity<List<Tournament>>(t, HttpStatus.OK);
    }
    
}
