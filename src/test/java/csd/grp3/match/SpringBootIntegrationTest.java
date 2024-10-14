package csd.grp3.match;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.grp3.user.User;
import csd.grp3.user.UserRepository;

/** Start an actual HTTP server listening at a random port */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootIntegrationTest {

	@LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";

	@Autowired
	/**
	 * Use TestRestTemplate for testing a real instance of your application as an
	 * external actor.
	 * TestRestTemplate is just a convenient subclass of RestTemplate that is
	 * suitable for integration tests.
	 * It is fault tolerant, and optionally can carry Basic authentication headers.
	 */
	private TestRestTemplate restTemplate;

	@Autowired
	private MatchRepository matches;

	@Autowired
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		// clear the database after each test
		users.deleteAll();
		matches.deleteAll();
	}

	@Test
	public void runApplication() {
		assertTrue(true);
	}

	@Test
	public void updateMatches_Success() throws Exception {
		// arrange
		URI uri = new URI(baseUrl + port + "/match/updateList");
		Match match1 = new Match();
		matches.save(match1);
		Match match2 = new Match();
		matches.save(match2);
		Match match3 = new Match();
		matches.save(match3);

		// update match results
		match1.setResult(1.0);
		match1.setBYE(true);
		match2.setResult(-1.0);
		match3.setResult(0.5);
		List<Match> expected = Arrays.asList(match1, match2, match3);
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN", 100));

		// act
		ResponseEntity<List<Match>> result = restTemplate.withBasicAuth("admin", "goodpassword")
			.exchange(uri, 
					HttpMethod.PUT, 
					new HttpEntity<List<Match>>(expected), 
					new ParameterizedTypeReference<List<Match>>() {});

		List<Match> resultList = result.getBody();

		// assert
		assertNotNull(resultList);
		assertEquals(200, result.getStatusCode().value());
		assertEquals(3, resultList.size());
		assertTrue(resultList.contains(match1));
		assertEquals(1.0, resultList.get(resultList.indexOf(match1)).getResult());
		assertTrue(resultList.get(resultList.indexOf(match1)).isBYE());
		assertTrue(resultList.contains(match2));
		assertEquals(-1.0, resultList.get(resultList.indexOf(match2)).getResult());
		assertTrue(resultList.contains(match3));
		assertEquals(0.5, resultList.get(resultList.indexOf(match3)).getResult());
	}
}
