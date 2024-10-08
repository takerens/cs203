package csd.grp3.user;

import java.util.List;

public interface UserService {
    User createNewUser(String username, String password);
    boolean login(String username, String password);
    List<User> findAll();
    User getUser(String username);
    // User changePassword(String newPassword);
}
