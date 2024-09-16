package services.tournament.src.main.java.com.cs203proj.tournament.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.tournament.src.main.java.com.cs203proj.tournament.model.Tournament;
import services.tournament.src.main.java.com.cs203proj.tournament.repo.TournamentRepo;

import java.util.*;
import java.io.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepo tournamentRepo;

    @GetMapping("/")
    public String home() {
        return "This is tournament system";
    }

    @GetMapping("/getAllTournaments")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        try {
            List<Tournament> tournamentList = new ArrayList<>();
            tournamentRepo.findAll().forEach(tournamentList::add);

            if (tournamentList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tournamentList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTournamentById/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournamentData = tournamentRepo.findById(id);

        if (tournamentData.isPresent()) {
            return new ResponseEntity<>(tournamentData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addTournament")
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        Tournament tournamentObj = tournamentRepo.save(tournament);

        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    }

    @PostMapping("/updateTournamentByID/{id}")
    public ResponseEntity<Tournament> updateTournamentById(@PathVariable Long id, @RequestBody Tournament newTournamentData) {
        Optional<Tournament> oldTournamentData = tournamentRepo.findById(id);

        if (oldTournamentData.isPresent()) {
            Tournament updatedTournamentData = oldTournamentData.get();
            updatedTournamentData.setDate(newTournamentData.getDate());
            // updatedTournamentData.setMatches(newTournamentData.getMatches());
            updatedTournamentData.setMaxElo(newTournamentData.getMaxElo());
            // updatedTournamentData.setParticipants(newTournamentData.getParticipants());
            updatedTournamentData.setMinElo(newTournamentData.getMinElo());

            Tournament tournamentObj = tournamentRepo.save(updatedTournamentData);
            return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteTournamentById/{id}")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
