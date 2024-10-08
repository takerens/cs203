package csd.grp3.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import csd.grp3.user.User;
import csd.grp3.usertournament.UserTournament;
import csd.grp3.usertournament.UserTournamentService;

import java.util.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private UserTournamentService userTournamentService;

    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournamentList = tournamentService.listTournaments();
        return ResponseEntity.status(HttpStatus.OK).body(tournamentList);
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<List<UserTournament>> getTournamentById(@PathVariable Long id) {
        List<UserTournament> tournamentData = userTournamentService.findByTournament(tournamentService.getTournament(id));
        return new ResponseEntity<>(tournamentData, HttpStatus.OK);
    }

    @PostMapping("/tournaments")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        Tournament tournamentObj = tournamentService.addTournament(tournament);
        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    }

    @PostMapping("/tournaments/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tournament> updateTournamentById(@PathVariable Long id, @RequestBody Tournament newTournamentData) {
        // Tournament tournamentObj = updateTournamentById(id, newTournamentData);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/tournaments/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<Void> withdrawPlayer(@RequestBody User player, @PathVariable Long id) {
        tournamentService.withdrawPlayer(player, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register/{id}")
    public ResponseEntity<Void> registerPlayer(@RequestBody User player, @PathVariable Long id) {
        tournamentService.registerPlayer(player, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
