package csd.grp3.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createNewUser(String username, String password);
    void login(String username, String password);
    List<User> findAll();
//    User changePassword(String newPassword);
}
