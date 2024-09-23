package csd.grp3.profile;

import org.springframework.stereotype.Controller;

@Controller
public class UserProfileController {
    
    private UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    //GET user elo
    //GET user display name
    //GET user tournament history
    //GET user tournement registered

    //PUT user elo - admin update player elo
    //PUT user tournament history - update history
    //PUT user tournamemt registered - update registered
    
}
