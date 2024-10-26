package csd.grp3.user;

public interface UserService {
    User createNewUser(String username, String password);
    User login(String username, String password);
    User findByUsername(String username);
    User changePassword(String username, String newPassword);
    void updateELO(User user, int newELO);
    void deleteUser(User user);
}
