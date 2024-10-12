package csd.grp3.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csd.grp3.user.User;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
        // Tournament tournamentObj = updateTournamentById(id, newTournamentData);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawPlayer(@RequestBody User player, @PathVariable Long id) {
        tournamentService.withdrawPlayer(player, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> registerPlayer(@RequestBody User player, @PathVariable Long id) {
        tournamentService.registerPlayer(player, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
