package csd.grp3.round;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long>{
    // Optional<Match> findByPlayer
}   
