package services.tournament.src.main.java.com.cs203proj.tournament.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import services.tournament.src.main.java.com.cs203proj.tournament.model.Tournament;

@Repository
public interface TournamentRepo extends JpaRepository<Tournament, Long> {
}
