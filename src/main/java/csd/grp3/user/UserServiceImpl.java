package csd.grp3.user;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public User createNewUser(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException("Username already taken");
//System.out.println("Username already exists. Please choose another username");
        }
//        else if (password.length() < 8) {
//            throw new WeakPasswordException("Password has to be at least 8 characters long");
////System.out.println("Does not meet password requirements");
//        }
        //Encode password given by user to store
        String encodedPassword = encoder.encode(password);

        return userRepository.save(new User(username, encodedPassword));
    }

    @Override
    public User login(String username, String password) {

        //If user does not exist, throw exception
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new InvalidCredentialsException("User does not exist");
        }

        //Get the password associated with the searched username
        User user = userRepository.findByUsername(username).get();
        String encodedPassword = user.getPassword();

        //Return if the password matches
        if (!encoder.matches(password, encodedPassword)) {
            throw new InvalidCredentialsException("Password does not match");
        }

        return user;
    }
    

    
    
    // public List<User> findAll() {
    //     return userRepository.findAll();
    // }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
    }
    
}
