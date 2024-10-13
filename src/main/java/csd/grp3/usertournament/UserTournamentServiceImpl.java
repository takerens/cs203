package csd.grp3.usertournament;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserTournamentServiceImpl implements UserTournamentService {

    private UserTournamentRepository userTournamentRepo;

    public UserTournament findRecord(Long tourneyID, String username) throws UserTournamentNotFoundException {
        return userTournamentRepo.findById_TournamentIdAndId_Username(tourneyID, username)
                .orElseThrow(() -> new UserTournamentNotFoundException());
    }

    @Override
    public List<User> getPlayers(Long tourneyID) {
        return userTournamentRepo.findRegisteredUsersByTournamentId(tourneyID);
    }

    @Override
    public List<User> getWaitingList(Long tourneyID) {
        return userTournamentRepo.findWaitlistUsersByTournamentId(tourneyID);
    }

    @Override
    public double getGamePoints(Long tourneyID, String username) {
        UserTournament ut = findRecord(tourneyID, username);
        return ut.getGamePoints();
    }

    @Override
    public void updateGamePoints(Long tourneyID, String username) {
        UserTournament ut = findRecord(tourneyID, username);
        ut.setGamePoints(ut.getGamePoints()); // getGamePoints adds MatchPoints
        ut.setMatchPoints(0); // reset match points
        userTournamentRepo.save(ut);
    }

    @Override
    public void updateMatchPoints(Long tourneyID, String username, double points) {
        UserTournament ut = findRecord(tourneyID, username);
        ut.setMatchPoints(points);
        userTournamentRepo.save(ut);
    }

    @Override
    // @Transactional
    public UserTournament updatePlayerStatus(Long tourneyID, String username, char status) {
        UserTournament ut = findRecord(tourneyID, username);
        ut.setStatus(status);
        return userTournamentRepo.save(ut);
    }

    @Override
    @Transactional
    public UserTournament add(Tournament tourney, User user, char status) {
        // Check if the UserTournament already exists
        Optional<UserTournament> existingUT = userTournamentRepo.findById_TournamentIdAndId_Username(tourney.getId(),
                user.getUsername());
        if (existingUT.isPresent()) {
            return updatePlayerStatus(tourney.getId(), user.getUsername(), status);
        }

        UserTournament ut = new UserTournament(
                new UserTournamentId(tourney.getId(), user.getUsername()),
                tourney, user, status, 0, 0);

        // Add the userTournament to both parent entities' lists
        tourney.getUserTournaments().add(ut);
        user.getUserTournaments().add(ut);

        return ut;
    }

    @Override
    @Transactional
    public void delete(Tournament tourney, User user) {
        // Remove the userTournament from both parent entities' lists
        UserTournament ut = findRecord(tourney.getId(), user.getUsername());
        tourney.getUserTournaments().remove(ut);
        user.getUserTournaments().remove(ut);
        userTournamentRepo.deleteById_TournamentIdAndId_Username(tourney.getId(), user.getUsername());
    }
}
