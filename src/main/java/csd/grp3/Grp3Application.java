package csd.grp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.grp3.user.UserRepository;
import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.user.User;

@SpringBootApplication
public class Grp3Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Grp3Application.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// JPA User Repository init
		UserRepository users = ctx.getBean(UserRepository.class);
		System.out.println("[Add Admin]: " + users.save(new User("admin", encoder.encode("password123"), "ROLE_ADMIN")).getUsername());
		System.out.println("[Add User]: " + users.save(new User("user", encoder.encode("user1234"), "ROLE_USER")).getUsername());

		// JPA User Repository init
		TournamentRepository ts = ctx.getBean(TournamentRepository.class);
		Tournament t = new Tournament();
		t.setTitle("Tournament A");
		System.out.println("[Add Tournament]: " + ts.save(t).getTitle());
		Tournament t1 = new Tournament();
		t1.setTitle("Tournament B");
		System.out.println("[Add Tournament]: " + ts.save(t1).getTitle());
	}

}
