package csd.grp3.usertournament;

import csd.grp3.user.*;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserTournamentRepository extends JpaRepository<UserTournament, UserTournamentId>{
    List<User> findUsersByTournamentId(Long tournamentId);
}
