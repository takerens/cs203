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
        throw new BadCredentialsException("Password does not match");
    }

    @Override
    public User changePassword(String username, String newPassword) {
        User user = findByUsername(username);
        user.setPassword(encoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public void updateELO(User tempUser, int newELO) {
        User user = findByUsername(tempUser.getUsername());
        user.setELO(newELO);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        login(user.getUsername(), user.getPassword());
        userRepository.delete(user);
    }
}
