package csd.grp3.profile;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private UserProfile profile;

    public UserProfileServiceImpl(UserProfile profile){
        this.profile = profile;
    }

	@Override
	public void modifyElo(int elo) {
		profile.setElo(elo);
	}

	@Override
	public void modifyDisplayName(String displayName) {
		profile.setDisplayName(displayName);
	}

	@Override
	public List<Tournament> showHistory() {
		return profile.getHistory();
	}

	@Override
	public List<Tournament> showRegistered() {
		return profile.getRegistered();
	}

    @Override
    public void addHistory(Tournament tournament){
        profile.addTournamentToHistory(tournament);
    }

    @Override
    public void addRegistered(Tournament tournament){
        profile.addTournamentToRegistered(tournament);
    }

    @Override
    public void removeRegistered(Tournament tournament){
        profile.removeTournamentFromRegistered(tournament);
    }
}
