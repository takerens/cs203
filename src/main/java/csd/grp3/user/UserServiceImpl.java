package csd.grp3.user;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User findByUsername(String username) throws UserNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public User createNewUser(String username, String password) throws BadCredentialsException{

        //Check if the username already exists, if it does throw exception
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadCredentialsException("Username already taken. Try a different username.");
        }

        // Encode password given by user to store
        String encodedPassword = encoder.encode(password);
        return userRepository.save(new User(username, encodedPassword));
    }

    @Override
    public User login(String username, String password) throws UserNotFoundException, BadCredentialsException{
        //Get the password associated with the searched username
        User user = findByUsername(username);

        //Return the user if the password matches
        if (encoder.matches(password, user.getPassword())) {
            return user;
        }
        //Else throw exception
        throw new BadCredentialsException("Password does not match");
    }

    @Override
    public User changePassword(String username, String newPassword) {
        User user = findByUsername(username);

        // If the new password is the same as the old one, throw an error
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new BadCredentialsException("New password should not be the same as the old one");
        }
        
        user.setPassword(encoder.encode(newPassword));
        return userRepository.save(user);
    }

    public String deleteByUsername(String username) {
        User user = findByUsername(username);
        userRepository.delete(user);
        return username;
    }

    @Override
    public void updateELO(User tempUser, int ELO) {
        User user = findByUsername(tempUser.getUsername());
        user.setELO(ELO);
        userRepository.save(user);
    }
}