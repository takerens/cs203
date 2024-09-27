package csd.grp3.profile;

import java.util.Optional;

import org.springframework.stereotype.Service;

import csd.grp3.user.User;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile getProfileByUser(User user) {
        return userProfileRepository.findByUser(user).orElse(null);
    }

    @Override
    public void modifyElo(User user, int newElo){
        Optional<UserProfile> userProfile = userProfileRepository.findByUser(user);
        if(userProfile.isPresent()){
            userProfile.get().setElo(newElo);
        } else {
            // throw profile not found exception
        }
    }

    @Override
    public void modifyDisplayName(User user, String displayName){

    }
    
    @Override
    public void addHistory(User user, Long id){

    }
    
    @Override
    public void addRegistered(User user, Long id){

    }
    
    @Override
    public void removeRegistered(User user, Long id){

    }
}
