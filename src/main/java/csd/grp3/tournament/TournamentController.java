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

import csd.grp3.user.User;


@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournamentList = tournamentService.listTournaments();
        return ResponseEntity.status(HttpStatus.OK).body(tournamentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournamentData = tournamentService.getTournament(id);
        return new ResponseEntity<>(tournamentData, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        Tournament tournamentObj = tournamentService.addTournament(tournament);
        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournamentById(@PathVariable Long id, @RequestBody Tournament newTournamentData) {
        Tournament tournamentObj = tournamentService.updateTournament(id, newTournamentData);
        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawUser(@RequestBody User user, @PathVariable Long id) {
        tournamentService.withdrawUser(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user, @PathVariable Long id) {
        tournamentService.registerUser(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
