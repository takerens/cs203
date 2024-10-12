package csd.grp3;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.grp3.user.UserRepository;
import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.tournament.TournamentService;
import csd.grp3.round.Round;
import csd.grp3.round.RoundRepository;
import csd.grp3.match.MatchRepository;
import csd.grp3.user.User;
import csd.grp3.match.Match;
import java.util.Arrays;

@SpringBootApplication
public class Grp3Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Grp3Application.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// JPA User Repository init
		UserRepository users = ctx.getBean(UserRepository.class);
		System.out.println("[Add Admin]: " + users.save(new User("Admin", encoder.encode("password123"), "ROLE_ADMIN", 0)).getUsername());
		System.out.println("[Add User]: " + users.save(new User("User100", encoder.encode("user1234"), "ROLE_USER", 100)).getUsername());
		System.out.println("[Add User]: " + users.save(new User("User0", encoder.encode("user1234"), "ROLE_USER", 0)).getUsername());
		System.out.println("[Add User]: " + users.save(new User("User110", encoder.encode("user1234"), "ROLE_USER", 110)).getUsername());
		System.out.println("[Add User]: " + users.save(new User("User150", encoder.encode("user1234"), "ROLE_USER", 150)).getUsername());
		System.out.println("[Add User]: " + users.save(new User("User120", encoder.encode("user1234"), "ROLE_USER", 120)).getUsername());
		System.out.println("[Add Bot]: " + users.save(new User("DEFAULT_BOT", encoder.encode("goodpassword"), "ROLE_USER", 0)).getUsername());

		// JPA User Repository init
		TournamentRepository ts = ctx.getBean(TournamentRepository.class);
		Tournament t = new Tournament();
		t.setTitle("Tournament A");
		t.setSize(4);
		t.setTotalRounds(2);
		t.setDate(LocalDateTime.of(2024, 9, 30, 15, 45));
		t.setId(1L);
		System.out.println("[Add Tournament]: " + ts.save(t).getTitle());
		Tournament t1 = new Tournament();
		t1.setTitle("Tournament B");
		t1.setSize(1);
		t1.setTotalRounds(1);
		t1.setDate(LocalDateTime.of(2024, 12, 20, 15, 0));
		System.out.println("[Add Tournament]: " + ts.save(t1).getTitle());
	}

}
