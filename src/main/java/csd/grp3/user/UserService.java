package csd.grp3.user;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User createNewUser(String username, String password);
    boolean login(String username, String password);
    List<User> findAll();
    Optional<User> findByUsername(String username);
//    User changePassword(String newPassword);
}
