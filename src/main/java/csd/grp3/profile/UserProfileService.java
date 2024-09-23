package csd.grp3.profile;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;

@Service
public interface UserProfileService {
    public void modifyElo(int newElo);
    public void modifyDisplayName(String displayName);
    public List<Tournament> showHistory();
    public List<Tournament> showRegistered();
}
