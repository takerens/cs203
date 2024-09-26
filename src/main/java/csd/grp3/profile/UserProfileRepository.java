package csd.grp3.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csd.grp3.user.User;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, User> {
        // define a derived query to find user-profile by username (or one to one map with user obj?)
        Optional<UserProfile> findByUser(User user);
}