package csd.grp3.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csd.grp3.round.Round;
import csd.grp3.user.User;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
    Optional<Match> findById(Long id);
    void deleteById(Long id);
    List<Match> findByRound(Round round);
    List<Match> findByBlackAndWhiteOrWhiteAndBlack(User user1black, User user2white, User user1white, User user2black);
    List<Match> findByBlackOrWhite(User user);
} 
