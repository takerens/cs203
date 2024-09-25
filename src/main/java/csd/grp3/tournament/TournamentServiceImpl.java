package csd.grp3.tournament;

import csd.grp3.match.Match;
import csd.grp3.match.MatchRepository;
import csd.grp3.player.Player;
import csd.grp3.user.User;
import csd.grp3.round.Round;
import csd.grp3.exception.MatchNotCompletedException;
import csd.grp3.match.Match;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired    
    private TournamentRepository tournaments;

    @Autowired
    private MatchRepository matches;

    public TournamentServiceImpl(TournamentRepository tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public List<Tournament> listTournaments() {
        return tournaments.findAll();
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        return tournaments.save(tournament);
    }

    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        return tournaments.findById(id).map(tournament -> {
            tournament.setTitle(newTournamentInfo.getTitle());
            return tournaments.save(tournament);
        }).orElse(null);
    }

    @Override
    public Tournament getTournament(Long id) {
        return tournaments.findById(id).orElse(null);
    }

    @Override
    public void deleteTournament(Long id) {
        tournaments.deleteById(id);
    }

    @Override
    public void registerPlayer(User player, Long id) throws TournamentNotFoundException {
        // check if got tournament
        Optional<Tournament> tournament = tournaments.findById(id);

        if (tournament.isPresent()) {
            // get tournament data & participant list from tournament
            Tournament tournamentData = tournament.get();
            List<User> participantList = tournamentData.getParticipants();

            // if tournament is full, we add to waitingList instead
            if (participantList.size() == tournamentData.getSize()) {
                List<User> waitingList = tournamentData.getWaitingList();
                waitingList.add(player);
                tournamentData.setWaitingList(waitingList);
                // else, we want to add to normal participantList
            } else {
                participantList.add(player);
                tournamentData.setParticipants(participantList);
            }

            // we save the tournament data back to database
            tournaments.save(tournamentData);
        } else {
            throw new TournamentNotFoundException(id);
        }
    }

    @Override
    public void withdrawPlayer(User player, Long id) {
        Optional<Tournament> tournament = tournaments.findById(id);
        if (tournament.isPresent()) {
            Tournament tournamentData = tournament.get();
            List<User> participantList = tournamentData.getParticipants();
            List<User> waitingList = tournamentData.getWaitingList();

            // Remove from participants
            if (participantList.remove(player)) {
                // If removed from participants, check waiting list
                if (!waitingList.isEmpty()) {
                    participantList.add(waitingList.remove(0)); // Move next from waiting list to participants
                }
                tournamentData.setParticipants(participantList);
                tournamentData.setWaitingList(waitingList);
                tournaments.save(tournamentData);
            }
        }
    }

    @Override
    public boolean tournamentExists(Long tournamentId) {
        return tournamentId != null && tournaments.existsById(tournamentId);
    }
  
    @Override
    public void updateResults(Round round) throws MatchNotCompletedException {
        List<Match> matches = round.getMatches();

        // check match ended
        for (Match match : matches) {
            // if match not complete
            if (match.getResult() == 0) {
                // throw exception that it's not complete
                throw new MatchNotCompletedException(match.getId());
            }
            else {
                // update player data with match results
                double result = match.getResult();
                User black = match.getBlack();
                User white = match.getWhite();
                if (result == -1) {
                    
                } else if (result == 1) {

                } else if (result == 0.5) {
                    
                }
            }
        }
        // update match results
        

    /**
     * Checks if 2 players have played each other in the tournament before.
     * Player order does not matter.
     * If no winner, returns "draw".
     * If never faced each other, returns "no direct encounter".
     * 
     * @param tournament - Tournament to check
     * @param player1 - First player
     * @param player2 - Second player
     * @return Username of winner 
     */
    public String directEncounterResultInTournament(Tournament tournament, Player player1, Player player2) {
        List<Match> directEncounters = matches.findByBlackAndWhiteOrWhiteAndBlack(player1, player2, player2, player1).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        if (directEncounters.size() == 0) {
            return "no direct encounter";
        }

        Match directEncounter = directEncounters.get(0);

        if (directEncounter.getResult() == -1) {
            return directEncounter.getBlack().getUsername();
        } else if (directEncounter.getResult() == 1) {
            return directEncounter.getWhite().getUsername();
        } else {
            return "draw";
        }
    }

    // Calculate Buchholz score for a player in a specific tournament (sum of opponents' match points)
    public int calculateBuchholzInTournament(Player player, Tournament tournament) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        int buchholzScore = 0;
        for (Match match : matchList) {
            Player opponent = match.getWhite().equals(player) ? match.getBlack() : match.getWhite();
            buchholzScore += opponent.getGamePoints();
        }
        return buchholzScore;
    }

    // Calculate Buchholz Cut 1 (exclude lowest opponent score) in a specific tournament
    public int calculateBuchholzCut1InTournament(Player player, Tournament tournament) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        List<Integer> opponentScores = new ArrayList<>();
        for (Match match : matchList) {
            Player opponent = match.getWhite().equals(player) ? match.getBlack() : match.getWhite();
            opponentScores.add(opponent.getGamePoints());
        }

        if (!opponentScores.isEmpty()) {
            opponentScores.remove(Collections.min(opponentScores)); // Exclude lowest score
        }

        return opponentScores.stream().mapToInt(Integer::intValue).sum();
    }
}


