// package csd.grp3.match;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import csd.grp3.round.Round;

// @Service
// public class MatchServiceImpl implements MatchService{
//     private MatchRepository matches;

//     public MatchServiceImpl(MatchRepository matches) {
//         this.matches = matches;
//     }

//     @Override
//     public List<Match> getRoundMatches(Round round) {
//         return matches.findByRound(round);
//     }

//     @Override
//     public Match getMatch(Long id){
//         return matches.findById(id).orElse(null);
//     }

//     @Override
//     public Match addMatch(Match match) {
//         return matches.save(match);
//     }

//     @Override 
//     public Match updateMatch(Long id, Match newMatch) {
//         return matches.findById(id).map(match -> {
//             match.setResult(newMatch.getResult());
//             match.setBYE(newMatch.isBYE());
//             return matches.save(match);
//         }).orElse(null);
//     }

//     @Override 
//     public void deleteMatch(Long id) {
//         matches.deleteById(id);
//     }
// }
