package csd.grp3.match;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.round.Round;
import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import jakarta.transaction.Transactional;

@Service
public class MatchServiceImpl implements MatchService{

    @Autowired
    private MatchRepository matches;

    @Override
    public Match createMatch(User white, User black, Round round) {
        return matches.save(new Match(white, black, round));
    }

    @Override
    public Match getMatch(Long id) throws MatchNotFoundException{
        return matches.findById(id)
            .orElseThrow(() -> new MatchNotFoundException());
    }

    @Override
    @Transactional
    public Match addMatch(Match match) {
        return matches.save(match);
    }

    @Override 
    public Match updateMatch(Long id, Match newMatch) {
        Match match = getMatch(id);
        match.setResult(newMatch.getResult());
        match.setBYE(newMatch.isBYE());
        return matches.save(match);
    }

    @Override 
    public void deleteMatch(Long id) {
        matches.deleteById(id);
    }

    @Override
    public List<Match> getRoundMatches(Round round) {
        return matches.findByRound(round);
    }

    @Override
    public List<Match> getUserMatches(User user) {
        return matches.findByBlackOrWhite(user, user);
    }

    @Override
    public List<Match> getMatchesBetweenTwoUsers(User user1, User user2) {
        return matches.findByBlackAndWhiteOrWhiteAndBlack(user1,user2,user2,user1);
    }
}
