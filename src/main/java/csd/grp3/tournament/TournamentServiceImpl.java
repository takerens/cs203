package csd.grp3.tournament;

import csd.grp3.user.User;

import java.util.List;
import java.util.Optional;

public class TournamentServiceImpl implements TournamentService {
    private TournamentRepository tournaments;

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
    public void registerPlayer(User player, Long id) {
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
}


