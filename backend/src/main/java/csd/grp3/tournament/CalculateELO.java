// package csd.grp3.tournament;

// import java.util.*;

// import csd.grp3.match.Match;
// import csd.grp3.user.User;

// public class CalculateELO {
//     /**
//      * Update user ELO via this formula documentation
//      * https://en.wikipedia.org/wiki/Chess_rating_system#Linear_approximation
//      * 
//      * @param matches - List of matches to consider
//      * @param user - User's ELO to update
//      * @return none
//      */
//     public static void update(List<Match> matches, User user) {
//         Integer userELO = user.getELO();
//         Integer totalDiffRating = 0;
//         Integer opponents = 0;
//         Integer wins = 0;
//         Integer loss = 0;
//         Integer developmentCoefficient = 40;    // TODO 1: placeholder for now
//         Integer classInterval = 200;            // TODO 2: placeholder for now

//         for (Match match : matches) {
//             // void match results as both player didnt play tgt
//             if (match.isBYE())
//                 continue;

//             if (match.getWhite().equals(user)) {
//                 if (match.getResult() == 1)
//                     wins++;
//                 else if (match.getResult() == -1)
//                     loss++;
                
//                 totalDiffRating += match.getBlack().getELO() - userELO;

//             } else {
//                 if (match.getResult() == 1)
//                     loss++;
//                 else if (match.getResult() == -1)
//                     wins++;

//                 totalDiffRating += match.getWhite().getELO() - userELO;
//             }

//             opponents++;
//         }

//         user.setELO(userELO + developmentCoefficient * (wins - loss) / 2 - (developmentCoefficient / 4 * classInterval) * totalDiffRating);
//     }
// }
