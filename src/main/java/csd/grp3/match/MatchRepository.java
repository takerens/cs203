package csd.grp3.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csd.grp3.user.User;
import csd.grp3.round.Round;
import org.springframework.lang.NonNull;


@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{

    @NonNull @Override Optional<Match> findById(@NonNull Long id);
    @NonNull List<Match> findByRound(@NonNull Round round);
    @Override void deleteById(@NonNull Long id);

    List<Match> findByBlackAndWhiteOrWhiteAndBlack(User player1black, User player2white, User player1white, User player2black);
    List<Match> findByBlackOrWhite(User player);

} 
