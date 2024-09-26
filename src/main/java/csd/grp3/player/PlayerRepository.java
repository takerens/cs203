package csd.grp3.player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long>{
    // Optional<Match> findByPlayer
}   
