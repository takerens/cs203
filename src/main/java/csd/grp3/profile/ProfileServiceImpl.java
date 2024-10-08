package csd.grp3.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentNotFoundException;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.user.User;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public Profile getProfileByUser(User user) {
        return profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
    }

    @Override
    public void modifyElo(User user, int newElo) {
        Profile profileData = profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
        profileData.setElo(newElo);
        profileRepository.save(profileData);
    }

    @Override
    public void modifyDisplayName(User user, String displayName) {
        Profile profileData = profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
        profileData.setDisplayName(displayName);
        profileRepository.save(profileData);
    }

    @Override
    public void addHistory(User user, Long id) {
        Profile profileData = profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
        Tournament tournamentData = tournamentRepository.findById(id).orElseThrow(() -> new TournamentNotFoundException(id));
        profileData.addTournamentToHistory(tournamentData);
        profileRepository.save(profileData);
    }

    @Override
    public void addRegistered(User user, Long id) {
        Profile profileData = profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
        Tournament tournamentData = tournamentRepository.findById(id).orElseThrow(() -> new TournamentNotFoundException(id));
        profileData.addTournamentToRegistered(tournamentData);
        profileRepository.save(profileData);
    }

    @Override
    public void removeRegistered(User user, Long id) {
        Profile profileData = profileRepository.findByUser(user).orElseThrow(ProfileNotFoundException::new);
        Tournament tournamentData = tournamentRepository.findById(id).orElseThrow(() -> new TournamentNotFoundException(id));
        profileData.removeTournamentFromRegistered(tournamentData);
        profileRepository.save(profileData);
    }
}
