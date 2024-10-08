package csd.grp3.user;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    // This function checks if password is alphanumeric and is 8 chars long
    private boolean checkPasswordRequirement(String password) {

        // Initialise booleans for password requirements
        boolean hasChar = false;
        boolean hasNum = false;
        boolean isAtLeast8Char = false;

        // Make password lowercase
        String tmpPassword = password.toLowerCase();

        // Check if password has char and num
        for (int i = 0; i < password.length(); i++) {
            char c = tmpPassword.charAt(i);
            if (c >= '0' && c <= '9') {
                hasNum = true;
            } else if (c >= 'a' && c <= 'z') {
                hasChar = true;
            }
        }

        // Check if password is at least 8 char long
        if (password.length() >= 8) {
            isAtLeast8Char = true;
        }

        // Print out error message
        if ((!hasChar || !hasNum) && isAtLeast8Char) {
            System.out.println("Password must contain both numbers and characters");
        } else if ((hasChar && hasNum) && !isAtLeast8Char) {
            System.out.println("Password must be at least 8 characters long");
        } else if ((!hasChar || !hasNum) && !isAtLeast8Char) {
            System.out.println("Password must contain both numbers and characters and be at least 8 characters long");
        } else {
            System.out.println("Password requirements met");
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public User createNewUser(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            System.out.println("Username already exists. Please choose another username");
            return null;
        } else if (!checkPasswordRequirement(password)) {
            System.out.println("Does not meet password requirements");
            return null;
        }

        // Encode password given by user to store
        String encodedPassword = this.encoder.encode(password);

        return userRepository.save(new User(username, encodedPassword));
    }

    @Override
    public boolean login(String username, String password) {

        // If user does not exist, immediately return false
        if (userRepository.findByUsername(username).isEmpty()) {
            System.out.println("USERNAME DOES NOT EXIST");
            return false;
        }

        // Get the password associated with the searched username
        User user = userRepository.findByUsername(username).get();
        String encodedPassword = user.getPassword();

        // Return if the password matches
        return this.encoder.matches(password, encodedPassword);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        Optional<User> optUser = userRepository.findByUsername(username);

        if (optUser.isPresent()) {
            return optUser.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}
