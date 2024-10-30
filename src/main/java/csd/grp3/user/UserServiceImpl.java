package csd.grp3.user;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import csd.grp3.jwt.JwtService;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
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
    public String login(String username, String password) throws UserNotFoundException, BadCredentialsException{
        //Get the password associated with the searched username
        User user = findByUsername(username);

        Authentication authentication = 
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //Return the user if the password matches
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(username);
        }
        //Else throw exception
        throw new BadCredentialsException("Username and Password do not match");
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
        return username;
    }

    @Override
    public void updateELO(User tempUser, int ELO) {
        User user = findByUsername(tempUser.getUsername());
        user.setELO(ELO);
        userRepository.save(user);
    }
}
