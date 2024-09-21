package csd.grp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import csd.grp3.user.UserRepository;
import csd.grp3.user.User;

@SpringBootApplication
public class Grp3Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Grp3Application.class, args);

		// JPA User Repository init
		UserRepository users = ctx.getBean(UserRepository.class);
		System.out.println("[Add Admin]: " + users.save(new User("admin", "password", "ROLE_ADMIN")).getUsername());
	}

}
