package csd.grp3.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
        // define a derived query to find user-profile by username (or one to one map with user obj?)
        Optional<UserProfile> findByUsername(String username);
}