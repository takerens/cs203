package csd.grp3.tournament;

import csd.grp3.user.User;

import java.util.List;

public interface TournamentService {
    List<Tournament> listTournaments();
    Tournament getTournament(Long id);
    Tournament addTournament(Tournament tournament);
    Tournament updateTournament(Long id, Tournament book);
    void deleteTournament(Long id);
    void addPlayer(User player, Long id);
}
