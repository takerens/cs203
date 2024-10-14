package csd.grp3.match;

import java.util.List;

import org.springframework.stereotype.Service;

import csd.grp3.round.Round;
import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;

@Service
public interface MatchService {
    Match createMatch(User white, User black, Round round);
    Match getMatch(Long id);
    Match addMatch(Match match);
    Match updateMatch(Long id, Match newMatch);
    void deleteMatch(Long id);
    List<Match> getRoundMatches(Round round);
    List<Match> getUserMatches(User user);
    List<Match> getMatchesBetweenTwoUsers(User user1, User user2);
}
