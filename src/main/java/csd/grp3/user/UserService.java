package csd.grp3.user;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    User createNewUser(String username, String password);
    User login(String username, String password);
    // List<User> findAll();
    User findByUsername(String username);
//    User changePassword(String newPassword);
}
