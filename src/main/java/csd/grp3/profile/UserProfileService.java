package csd.grp3.profile;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;

@Service
public interface UserProfileService {

    public UserProfile getProfileByUser(User user);

    public void modifyElo(int newElo);
    public void modifyDisplayName(String displayName);
    public List<Tournament> showHistory();
    public List<Tournament> showRegistered();
    public void addHistory(Tournament tournament);
    public void addRegistered(Tournament tournament);
    public void removeRegistered(Tournament tournament);
}
