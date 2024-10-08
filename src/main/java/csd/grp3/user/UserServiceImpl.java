package csd.grp3.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User createNewUser(String username, String password) throws UsernameAlreadyTakenException {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException("Username already taken");
        }
        //Encode password given by user to store
        String encodedPassword = encoder.encode(password);
        return userRepository.save(new User(username, encodedPassword));
    }

    public User login(String username, String password) throws InvalidCredentialsException{

        //If user does not exist, throw exception
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new InvalidCredentialsException("User does not exist");
        }

        //Get the password associated with the searched username
        User user = userRepository.findByUsername(username).get();
        String encodedPassword = user.getPassword();

        //Throw exception if password does not match
        if (!encoder.matches(password, encodedPassword)) {
            throw new InvalidCredentialsException("Password does not match");
        }
        return user;
    }

    public User findByUsername(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username)); // Throw an exception if not found
    }
}
