package csd.grp3.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepo;

    @GetMapping("/")
    public String home() {
        return "This is tournament system";
    }

    @GetMapping("/tournaments")
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

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Optional<Tournament> tournamentData = tournamentRepo.findById(id);

        if (tournamentData.isPresent()) {
            return new ResponseEntity<>(tournamentData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tournaments/{title}")
    public ResponseEntity<Tournament> getTournamentByTitle(@PathVariable String title) {
        Tournament tournamentData = tournamentRepo.findByTitle(title);
        
        if (tournamentData != null) {
            return new ResponseEntity<>(tournamentData, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/tournaments")
    public ResponseEntity<Tournament> addTournament(@RequestBody Tournament tournament) {
        Tournament tournamentObj = tournamentRepo.save(tournament);

        return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
    }

    @PostMapping("/tournaments/{id}")
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

            Tournament tournamentObj = tournamentRepo.save(updatedTournamentData);
            return new ResponseEntity<>(tournamentObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/tournaments/{title}")
    public ResponseEntity<Tournament> updateTournamentByTitle(@PathVariable String title, @RequestBody Tournament newTournamentData) {
        Tournament oldTournamentData = tournamentRepo.findByTitle(title);

        if (oldTournamentData != null) {
            Tournament updatedTournamentData = oldTournamentData;
            updatedTournamentData.setTitle(newTournamentData.getTitle());
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

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<HttpStatus> deleteTournamentById(@PathVariable Long id) {
        tournamentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tournaments/{title}")
    public ResponseEntity<HttpStatus> deleteTournamentByTitle(@PathVariable String title) {
        tournamentRepo.deleteByTitle(title);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}