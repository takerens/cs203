package csd.grp3.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>{
    Tournament findByTitle(String title);
    Tournament deleteByTitle(String title);
} 