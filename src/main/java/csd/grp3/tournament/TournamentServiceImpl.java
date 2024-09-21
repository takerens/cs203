package csd.grp3.tournament;

import csd.grp3.user.User;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
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
    public void addPlayer(User player, Long id) {
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


}