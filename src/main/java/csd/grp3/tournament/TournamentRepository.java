package csd.grp3.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>{
    // Optional<Tournament> findById(Long id);
    Optional<Tournament> findByTitle(String title);
    Optional<Tournament> deleteByTitle(String title);
    List<Tournament> findAll();
} 
