package csd.grp3.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
//    public UserService(UserRepository users) {
//        this.users = users;
//    }
//
//
//    public void createAccount(User newUser) {
//        if (users.findByUsername(newUser.getUsername()).isPresent()) {
//            System.out.println("Username already exists. Please choose another username");
//        } else {
//            users.save(new User(newUser.getUsername(), newUser.getPassword()));
//        }
//    }

    User createUser(String username, String password);

    void login(String username, String password);
//    User changePassword(String newPassword);
}
