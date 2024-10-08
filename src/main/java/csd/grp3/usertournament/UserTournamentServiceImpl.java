package csd.grp3.usertournament;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;

@Service
public class UserTournamentServiceImpl implements UserTournamentService {

    private UserTournamentRepository userTournamentRepo;

    public UserTournamentServiceImpl(UserTournamentRepository userTournamentRepo) {
        this.userTournamentRepo = userTournamentRepo;
    }

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
    public void updateGamePoints(Long tourneyID, String username, double increment) {
        UserTournament ut = findRecord(tourneyID, username);
        double updatedGamePoints = ut.getGamePoints() + increment;
        ut.setGamePoints(updatedGamePoints);
        userTournamentRepo.save(ut);
    }

    @Override
    public UserTournament updatePlayerStatus(Long tourneyID, String username, char status) {
        UserTournament ut = findRecord(tourneyID, username);
        ut.setStatus(status);
        return userTournamentRepo.save(ut);
    }

    @Override
    public UserTournament add(Tournament tourney, User user, char status) {
        Long tourneyID = tourney.getId();
        String username = user.getUsername();
        UserTournamentId utId = new UserTournamentId(tourneyID, username);        
        return userTournamentRepo.save(new UserTournament(utId, tourney, user, status, 0));
    }

    @Override
    public void delete(Long tourneyID, String username) {
        userTournamentRepo.deleteById_TournamentIdAndId_Username( tourneyID,  username);
    }
    
}
