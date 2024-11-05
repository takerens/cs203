package csd.grp3.usertournament;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import csd.grp3.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserTournamentServiceImpl implements UserTournamentService {

    @Autowired
    private UserTournamentRepository userTournamentRepo;

<<<<<<< HEAD
    @Autowired
    private UserService userService;

    public UserTournament findRecord(Long tourneyID, String username) throws UserTournamentNotFoundException {
        return userTournamentRepo.findById_TournamentIdAndId_Username(tourneyID, username)
=======
    public UserTournament findRecord(Long tournamentID, String username) throws UserTournamentNotFoundException {
        return userTournamentRepo.findById_TournamentIdAndId_Username(tournamentID, username)
>>>>>>> frontendcopyformerging
                .orElseThrow(() -> new UserTournamentNotFoundException());
    }

    /**
     * Retrieves a list of players registered for a given tournament, excluding the default bot user.
     *
     * @param tourneyID the ID of the tournament for which to retrieve the players
     * @return a list of users registered for the specified tournament, excluding the default bot user
     */
    @Override
<<<<<<< HEAD
    public List<User> getPlayers(Long tourneyID) {
        List<User> players = userTournamentRepo.findRegisteredUsersByTournamentId(tourneyID);
        User user = userService.findByUsername("DEFAULT_BOT");
        players.remove(user);
        return players;
=======
    public List<User> getPlayers(Long tournamentID) {
        return userTournamentRepo.findRegisteredUsersByTournamentId(tournamentID);
>>>>>>> frontendcopyformerging
    }

    @Override
    public List<User> getWaitingList(Long tournamentID) {
        return userTournamentRepo.findWaitlistUsersByTournamentId(tournamentID);
    }

    @Override
    public double getGamePoints(Long tournamentID, String username) {
        UserTournament userTournament = findRecord(tournamentID, username);
        return userTournament.getGamePoints();
    }

    @Override
    public void updateGamePoints(Long tournamentID, String username) {
        UserTournament userTournament = findRecord(tournamentID, username);
        userTournament.setGamePoints(userTournament.getGamePoints()); // getGamePoints adds MatchPoints
        userTournament.setMatchPoints(0); // reset match points
        userTournamentRepo.save(userTournament);
    }

    @Override
    public void updateMatchPoints(Long tournamentID, String username, double points) {
        UserTournament userTournament = findRecord(tournamentID, username);
        userTournament.setMatchPoints(points);
        userTournamentRepo.save(userTournament);
    }

    @Override
    // @Transactional
    public UserTournament updatePlayerStatus(Long tournamentID, String username, char status) {
        UserTournament userTournament = findRecord(tournamentID, username);
        userTournament.setStatus(status);
        return userTournamentRepo.save(userTournament);
    }

    @Override
    @Transactional
    public UserTournament add(Tournament tournament, User user, char status) {
        // Check if the UserTournament already exists
        Optional<UserTournament> existinguserTournament = userTournamentRepo.findById_TournamentIdAndId_Username(tournament.getId(),
                user.getUsername());
        if (existinguserTournament.isPresent()) {
            return updatePlayerStatus(tournament.getId(), user.getUsername(), status);
        }

        UserTournament userTournament = new UserTournament(
                new UserTournamentId(tournament.getId(), user.getUsername()),
                tournament, user, status, 0, 0);

        // Add the userTournament to both parent entities' lists
        tournament.getUserTournaments().add(userTournament);
        user.getUserTournaments().add(userTournament);

        return userTournament;
    }

    @Override
    @Transactional
    public void delete(Tournament tournament, User user) {
        // Remove the userTournament from both parent entities' lists
        UserTournament userTournament = findRecord(tournament.getId(), user.getUsername());
        tournament.getUserTournaments().remove(userTournament);
        user.getUserTournaments().remove(userTournament);
        userTournamentRepo.deleteById_TournamentIdAndId_Username(tournament.getId(), user.getUsername());
    }
}
