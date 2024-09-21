package csd.grp3.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User createUser(String username, String password) {
        return new User(username, password);
    }
}
