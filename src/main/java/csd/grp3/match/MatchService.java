package csd.grp3.match;

import java.util.List;

import csd.grp3.round.Round;

public interface MatchService {
    List<Match> getRoundMatches(Round round);
    Match getMatch(Long id);
    Match addMatch(Match match);
    Match updateMatch(Long id, Match match);
    void deleteMatch(Long id);
}
