package csd.grp3.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
    public User login(String username, String password) throws UsernameNotFoundException{
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
    public User changePassword(String username, String password) {
        User user = findByUsername(username);
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }
}
