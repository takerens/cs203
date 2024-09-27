package csd.grp3.profile;

import java.util.Optional;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.user.User;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final TournamentRepository tournamentRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository,
            TournamentRepository tournamentRepository) {
        this.profileRepository = profileRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Profile getProfileByUser(User user) {
        return profileRepository.findByUser(user).orElse(null);
    }

    @Override
    public void modifyElo(User user, int newElo) {
        Optional<Profile> profile = profileRepository.findByUser(user);
        Profile profileData = profile.get();

        if (profile.isPresent()) {
            profileData.setElo(newElo);
        } else {
            // throw ProfileNotFoundException
        }
        profileRepository.save(profileData);
    }

    @Override
    public void modifyDisplayName(User user, String displayName) {
        Optional<Profile> profile = profileRepository.findByUser(user);
        Profile profileData = profile.get();

        if (profile.isPresent()) {
            profileData.setDisplayName(displayName);
        } else {
            // throw ProfileNotFoundException
        }
        profileRepository.save(profileData);
    }

    @Override
    public void addHistory(User user, Long id) {
        Optional<Profile> profile = profileRepository.findByUser(user);
        // .orElseThrow(() -> new ProfileNotFoundException("profile not found"));
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        // .orElseThrow(() -> new TournamentNotFoundException("tournament not found"));

        Profile profileData = profile.get();
        Tournament tournamentData = tournament.get();
        profileData.addTournamentToHistory(tournamentData);
        profileRepository.save(profileData);
    }

    @Override
    public void addRegistered(User user, Long id) {
        Optional<Profile> profile = profileRepository.findByUser(user);
        // .orElseThrow(() -> new ProfileNotFoundException("profile not found"));
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        // .orElseThrow(() -> new TournamentNotFoundException("tournament not found"));

        Profile profileData = profile.get();
        Tournament tournamentData = tournament.get();
        profileData.addTournamentToRegistered(tournamentData);
        profileRepository.save(profileData);
    }

    @Override
    public void removeRegistered(User user, Long id) {
        Optional<Profile> profile = profileRepository.findByUser(user);
        // .orElseThrow(() -> new ProfileNotFoundException("profile not found"));
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        // .orElseThrow(() -> new TournamentNotFoundException("tournament not found"));

        Profile profileData = profile.get();
        Tournament tournamentData = tournament.get();
        profileData.removeTournamentFromRegistered(tournamentData);
        profileRepository.save(profileData);
    }
}
