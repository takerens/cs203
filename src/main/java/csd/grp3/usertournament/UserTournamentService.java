package csd.grp3.usertournament;

import java.util.List;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;

public interface UserTournamentService {
    List<User> getPlayers(Long tourneyID);
    List<User> getWaitingList(Long tourneyID);
    double getGamePoints(Long tourneyID, String username);
    UserTournament updateGamePoints(Long tourneyID, String username, double gamePoints);
    UserTournament updatePlayerStatus(Long tourneyID, String username, char status);
    UserTournament add(Tournament tourneyID, User username, char status);
    void delete(Long tourneyID, String username);
}
