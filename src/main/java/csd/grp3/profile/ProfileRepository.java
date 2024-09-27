package csd.grp3.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csd.grp3.user.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, User> {
    Optional<Profile> findByUser(User user);
}
