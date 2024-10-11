package csd.grp3.tournament;

import csd.grp3.user.User;
import csd.grp3.round.Round;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TournamentService {
    List<Tournament> listTournaments();
    Tournament getTournament(Long id);
    Tournament addTournament(Tournament tournament);
    Tournament updateTournament(Long id, Tournament tournament);
    void deleteTournament(Long id);
    void registerUser(User user, Long id);
    void withdrawUser(User user, Long id);
    void addRound(Long id);
    void updateResults(Round round);
    void endTournament(Long id);
    List<Tournament> getTournamentAboveMin(int ELO);
    List<Tournament> getTournamentBelowMax(int ELO);
    List<Tournament> getTournamentAboveMinBelowMax(int minELO, int maxELO);
    List<Tournament> getUserEligibleTournament(User user);
    double calculateBuchholzInTournament(User user, Tournament tournament);
    double calculateBuchholzCut1InTournament(User user, Tournament tournament);
    Round createPairings(Tournament tournament);
    
    // public static void update(List<Match> matches, User user);
    // private --
    // boolean isNextColourWhite(User user, Tournament tournament)
    // boolean hasPlayedBefore(User user1, User user2, Tournament tournament)
    // boolean isColourSuitable(User user, Tournament tournament, String nextColour)
    // Match createMatchWithUserColour(User user1, String user1Colour, User user2, Round round)
    // Match handleBYE(User worst, String color, Round round);

}
