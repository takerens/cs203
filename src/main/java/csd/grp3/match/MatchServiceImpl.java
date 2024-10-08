package csd.grp3.match;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import csd.grp3.round.Round;
import csd.grp3.user.User;

@Service
public class MatchServiceImpl implements MatchService{
    private MatchRepository matches;

    public MatchServiceImpl(MatchRepository matches) {
        this.matches = matches;
    }

    @Override
    public Match getMatch(Long id) throws MatchNotFoundException{
        Optional<Match> optMatch = matches.findById(id);
        
        if (optMatch.isPresent()) {
            return optMatch.get();
        } else {
            throw new MatchNotFoundException();
        }
    }

    @Override
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
        return matches.findByBlackOrWhite(user);
    }

    @Override
    public List<Match> getMatchesBetweenTwoUsers(User user1, User user2) {
        return matches.findByBlackAndWhiteOrWhiteAndBlack(user1,user2,user2,user1);
    }
}
