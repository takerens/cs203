package csd.grp3.user;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            System.out.println("Username already exists. Please choose another username");
            return null;
        }

        return userRepository.save(new User(username, password));
    }

    public void login(String username, String password) {

    }
}
