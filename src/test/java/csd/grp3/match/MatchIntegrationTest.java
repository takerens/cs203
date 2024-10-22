package csd.grp3.match;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import csd.grp3.round.Round;
import csd.grp3.round.RoundRepository;
import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.user.User;
import csd.grp3.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MatchIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        matchRepository.deleteAll();
        roundRepository.deleteAll();
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void updateMatch_Success() throws Exception {
        // Step 1: Create users and save them
        User white = new User("whitePlayer1", "password123");
        User black = new User("blackPlayer1", "password123");
        userRepository.saveAll(Arrays.asList(white, black));

        // Log users to confirm they are saved correctly
        System.out.println("Saved Users: " + objectMapper.writeValueAsString(userRepository.findAll()));

        // Step 2: Create a tournament and round, then save them
        Tournament tournament = new Tournament("Sample Tournament", 1200, 2000, LocalDateTime.now().plusDays(1), 8, 3);
        tournamentRepository.save(tournament);

        Round round = new Round(tournament);
        roundRepository.save(round);

        // Log tournament and round to confirm they are saved correctly
        System.out.println("Saved Tournament: " + objectMapper.writeValueAsString(tournament));
        System.out.println("Saved Round: " + objectMapper.writeValueAsString(round));

        // Step 3: Create and save a match
        Match match = new Match(white, black, round);
        match = matchRepository.save(match);
        System.out.println("Match created with ID: " + match.getId());

        // Ensure match is created
        assertNotNull(match.getId());

        // Log match to confirm details
        System.out.println("Created Match: " + objectMapper.writeValueAsString(match));

        // Step 4: Verify the match is present in the database
        Match fetchedMatch = matchRepository.findById(match.getId()).orElse(null);
        System.out.println("Fetched Match from DB: " + objectMapper.writeValueAsString(fetchedMatch));
        assertNotNull(fetchedMatch, "Match should be found in the database before the update.");

        // Step 5: Modify the match object and ensure the same ID is used
        match.setResult(1);
        match.setBYE(true);
        URI uri = new URI(baseUrl + port + "/match/updateList");

        // Log the match data to confirm it
        System.out.println("Updating Match ID: " + match.getId());
        System.out.println("Updating Match Data: " + objectMapper.writeValueAsString(match));

        // Make sure to send the correct JSON format
        HttpEntity<List<Match>> requestEntity = new HttpEntity<>(Arrays.asList(match));

        // Log the JSON payload
        String jsonPayload = objectMapper.writeValueAsString(Arrays.asList(match));
        System.out.println("JSON Payload Sent: " + jsonPayload);

        // Send PUT request
        ResponseEntity<Void> result = restTemplate.exchange(
            uri,
            HttpMethod.PUT,
            requestEntity,
            new ParameterizedTypeReference<Void>() {}
        );

        // Log the response
        System.out.println("Response Status Code: " + result.getStatusCode().value());

        // Verify the status code is 200 OK
        Assertions.assertEquals(200, result.getStatusCode().value());

        // Step 6: Verify the match was updated correctly in the database
        Match updatedMatch = matchRepository.findById(match.getId()).orElseThrow(() -> new MatchNotFoundException("Match not found"));
        System.out.println("Updated Match Data: " + objectMapper.writeValueAsString(updatedMatch));

        Assertions.assertEquals(1, updatedMatch.getResult());
        Assertions.assertTrue(updatedMatch.isBYE());
    }
}
