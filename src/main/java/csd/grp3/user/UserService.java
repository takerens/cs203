package csd.grp3.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createNewUser(String username, String password);
    void login(String username, String password);
//    User changePassword(String newPassword);
}
