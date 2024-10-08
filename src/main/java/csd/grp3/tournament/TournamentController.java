package csd.grp3.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import csd.grp3.user.User;

import jakarta.servlet.http.HttpSession;

import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        // List<Tournament> tournamentList = tournamentRepo.findAll();
        // System.out.println(tournamentList);
        // should be like this (below)
        List<Tournament> tournamentList = tournamentService.listTournaments();
        return ResponseEntity.status(HttpStatus.OK).body(tournamentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournamentData = tournamentService.getTournament(id);
        return new ResponseEntity<>(tournamentData, HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        Tournament tournamentObj = tournamentRepo.save(tournament);

        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tournament> updateTournamentById(@PathVariable Long id, @RequestBody Tournament newTournamentData) {
        Optional<Tournament> oldTournamentData = tournamentRepo.findById(id);

        if (oldTournamentData.isPresent()) {
            Tournament updatedTournamentData = oldTournamentData.get();
            updatedTournamentData.setTitle(newTournamentData.getTitle());
            updatedTournamentData.setDate(newTournamentData.getDate());
            // updatedTournamentData.setMatches(newTournamentData.getMatches());
            updatedTournamentData.setMaxElo(newTournamentData.getMaxElo());
            // updatedTournamentData.setParticipants(newTournamentData.getParticipants());
            updatedTournamentData.setMinElo(newTournamentData.getMinElo());
            updatedTournamentData.setSize(newTournamentData.getSize());
            // updatedTournamentData.setWaitingList(newTournamentData.getWaitingList());

            Tournament tournamentObj = tournamentRepo.save(updatedTournamentData);
            return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @DeleteMapping("/tournaments/{title}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<HttpStatus> deleteTournamentByTitle(@PathVariable String title) {
    //     tournamentRepo.deleteByTitle(title);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    @DeleteMapping("{id}/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody User user , @PathVariable Long id) {
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
        return null; // List Users in order of GamePoints
    }
    
}
