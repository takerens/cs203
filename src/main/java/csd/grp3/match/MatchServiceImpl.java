package csd.grp3.match;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.round.Round;
import csd.grp3.user.User;
import jakarta.transaction.Transactional;

@Service
public class MatchServiceImpl implements MatchService{

    @Autowired
    private MatchRepository matches;

    /**
     * Creates a match object for the Round
     * 
     * @param white 
     * @param black
     * @param round
     * @return Match object
    */
    @Override
    public Match createMatch(User white, User black, Round round) {
        return matches.save(new Match(white, black, round));
    }

    /**
     * Get a match object via its id
     * 
     * @param id Match id
     * @return Match object
     */
    @Override
    public Match getMatch(Long id) throws MatchNotFoundException{
        return matches.findById(id)
            .orElseThrow(() -> new MatchNotFoundException());
    }

    /**
     *  Saves a match object to database
     * 
     * @param match
     * @return Match object added
     */
    @Override
    @Transactional
    public Match addMatch(Match match) {
        return matches.save(match);
    }

    /**
     * Update match object referenced by id
     * 
     * @param id target match to update
     * @param newMatch updated match details
     * @return Updated Match object
     */
    @Override 
    public Match updateMatch(Long id, Match newMatch) {
        Match match = getMatch(id);
        match.setResult(newMatch.getResult());
        match.setBYE(newMatch.isBYE());
        return matches.save(match);
    }

    /**
     * Deletes a match by id from repository 
     * 
     * @param id 
     */
    @Override 
    public void deleteMatch(Long id) {
        matches.deleteById(id);
    }

    /**
     * Get all matches in round
     * 
     * @param round 
     */
    @Override
    public List<Match> getRoundMatches(Round round) {
        return matches.findByRound(round);
    }

    /**
     * Get all matches played by user.
     * Across all tournaments and rounds
     * 
     * @param user
     * @return List<Match>
     */
    @Override
    public List<Match> getUserMatches(User user) {
        return matches.findByBlackOrWhite(user, user);
    }

    /**
     * Get all matches played between 2 users.
     * Across all tournaments and rounds
     * 
     * @param user1
     * @param user2
     * @return List<Match>
     */
    @Override
    public List<Match> getMatchesBetweenTwoUsers(User user1, User user2) {
        return matches.findByBlackAndWhiteOrWhiteAndBlack(user1,user2,user2,user1);
    }
}
