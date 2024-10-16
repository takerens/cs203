package csd.grp3.user;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User createNewUser(String username, String password) throws BadCredentialsException{

        //Check if the username already exists, if it does throw exception
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadCredentialsException("Username already exists");
        }

        // Encode password given by user to store
        String encodedPassword = encoder.encode(password);

        return userRepository.save(new User(username, encodedPassword));
    }

    @Override
    public User login(String username, String password) throws UserNotFoundException, BadCredentialsException{
        //Get the password associated with the searched username
        User user = findByUsername(username);
        String encodedPassword = user.getPassword();

        //Return the user if the password matches
        if (encoder.matches(password, encodedPassword)) {
            return user;
        }
        //Else throw exception
        throw new BadCredentialsException("Password does not match");
    }

    @Override
    public User changePassword(String username, String newPassword) throws UserNotFoundException, BadCredentialsException{
        User user = findByUsername(username);
        //Only change the password if it is different
        if (!encoder.matches(newPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            return userRepository.save(user);
        }

        throw new BadCredentialsException("Password already in use");        
    }

    public String deleteByUsername(String username) {
        User user = findByUsername(username);
        userRepository.delete(user);
        return username + " has been deleted";
    }

    @Override
    public void updateELO(User tempUser, int ELO) {
        User user = findByUsername(tempUser.getUsername());
        user.setELO(ELO);
        userRepository.save(user);
    }
}
