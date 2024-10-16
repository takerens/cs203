package csd.grp3.user;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll(); 
    }

    @Test
    public void registerUser_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/signup");

        User newUser = new User("testuser", "password123");

        ResponseEntity<User> result = restTemplate.postForEntity(uri, newUser, User.class);


        Assertions.assertEquals(201, result.getStatusCode().value()); 
        Assertions.assertEquals(newUser.getUsername(), result.getBody().getUsername()); 
        Assertions.assertTrue(encoder.matches(newUser.getPassword(), result.getBody().getPassword())); 
    }

    @Test
    public void loginUser_Success() throws Exception {
  
        User newUser = new User("testuser", encoder.encode("password123"));
        userRepository.save(newUser);


        URI uri = new URI(baseUrl + port + "/login");

        ResponseEntity<User> result = restTemplate.postForEntity(uri, newUser, User.class);

        Assertions.assertEquals(200, result.getStatusCode().value()); 
        Assertions.assertEquals(newUser.getUsername(), result.getBody().getUsername()); 
    }

    @Test
    public void loginUser_Failure() throws Exception {

        URI uri = new URI(baseUrl + port + "/login");

        User invalidUser = new User("wronguser", "wrongpassword");

        ResponseEntity<User> result = restTemplate.postForEntity(uri, invalidUser, User.class);

        Assertions.assertEquals(400, result.getStatusCode().value()); 
    }

    @Test
    public void changePassword_Success() throws Exception {

        User newUser = new User("testuser", encoder.encode("password123"));
        userRepository.save(newUser);

        URI uri = new URI(baseUrl + port + "/user/changePassword");

        User updatedUser = new User("testuser", "newpassword123");

        HttpEntity<User> request = new HttpEntity<>(updatedUser);

        ResponseEntity<User> result = restTemplate.withBasicAuth("testuser", "password123")
                .exchange(uri, HttpMethod.PUT, request, User.class);

        Assertions.assertEquals(200, result.getStatusCode().value()); // Check if status code is OK
        Assertions.assertTrue(encoder.matches(updatedUser.getPassword(), result.getBody().getPassword())); // Ensure password is updated and hashed
    }

    @Test
    public void deleteUser_Success() throws Exception {
        // First, register the user
        User newUser = new User("testuser", encoder.encode("password123"));
        userRepository.save(newUser);


        URI uri = new URI(baseUrl + port + "/profile/testuser");

        ResponseEntity<String> result = restTemplate.withBasicAuth("testuser", "password123")
                .exchange(uri, HttpMethod.DELETE, null, String.class);


        Assertions.assertEquals(200, result.getStatusCode().value()); 
        Assertions.assertEquals(Optional.empty(), userRepository.findByUsername("testuser")); 
    }

}