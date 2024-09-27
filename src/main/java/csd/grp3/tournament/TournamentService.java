package csd.grp3.tournament;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.player.Player;
import csd.grp3.round.Round;
import csd.grp3.user.User;

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
    void updateResults(Round round);
    String directEncounterResultInTournament(Tournament tournament, Player player1, Player player2);
}
