package csd.grp3.usertournament;

import csd.grp3.user.*;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserTournamentRepository extends JpaRepository<UserTournament, UserTournamentId>{
    List<User> findUsersByTournamentId(Long tournamentId);

    @Query("SELECT ut from UserTournament ut where ut.tournamentId = :tournamentId and ut.username = :username")
    Optional<UserTournament> findUserTournamentByTournamentIdAndUsername(
        @Param("tournamentId") Long tournamentId, 
        @Param("username") String username);

    @Query("SELECT ut.user FROM UserTournament ut WHERE ut.tournamentId = :tournamentId AND ut.status = 'r'")
    List<User> findRegisteredUsersByTournamentId(@Param("tournamentId") Long tournamentId);
    
    @Query("SELECT ut.user FROM UserTournament ut WHERE ut.tournamentId = :tournamentId AND ut.status = 'w'")
    List<User> findWaitlistUsersByTournamentId(@Param("tournamentId") Long tournamentId);

    // @Query("SELECT ut.gamePoints FROM UserTournament ut WHERE ut.tournamentId = :tournamentId AND ut.username = :username")
    // double getGamePointsByTournamentIdAndUsername(
    //     @Param("tournamentId") Long tournamentId, 
    //     @Param("username") String username);

    // @Query("UPDATE UserTournament ut SET ut.gamePoints = :newGamePoints WHERE ut.tournamentId = :tournamentId AND ut.username = :username")
    // UserTournament updateGamePointsByTournamentIdAndUsername(
    //     @Param("tournamentId") Long tournamentId,
    //     @Param("username") String username,
    //     @Param("newGamePoints") double increment);

    // @Query("UPDATE UserTournament ut SET ut.status = :updatedStatus WHERE ut.tournamentId = :tournamentId AND ut.username = :username")
    // UserTournament updatePlayerStatusByTournamentIdAndUsername(
    //     @Param("tournamentId") Long tournamentId, 
    //     @Param("username") String username, 
    //     @Param("updatedStatus") char newStatus);
    void deleteByTournamentIdAndUsername(Long tourneyId, String username);
}
