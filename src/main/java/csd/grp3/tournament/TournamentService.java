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
    Tournament updateTournament(Long id, Tournament book);
    void deleteTournament(Long id);
    void registerPlayer(User player, Long id);
    void withdrawPlayer(User player, Long id);
    boolean tournamentExists(Long tournamentId);
    void addRound(Long id);
    void updateResults(Round round);
    String directEncounterResultInTournament(Tournament tournament, User player1, User player2);
}
