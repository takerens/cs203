package csd.grp3.tournament;

import csd.grp3.match.*;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import csd.grp3.exception.MatchNotCompletedException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentRepository tournaments;

    @Autowired
    private MatchService matchService;

    public TournamentServiceImpl(TournamentRepository tournaments, MatchService matchService) {
        this.tournaments = tournaments;
        this.matchService = matchService;
    }

    @Override
    public List<Tournament> listTournaments() {
        return tournaments.getAllTournaments();
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        return tournaments.save(tournament);
    }

    @Override
    public Tournament getTournament(Long id) throws TournamentNotFoundException {
        return getTournament(id);
    }

    // TODO Do this function properly accourding to baseline()
    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        Tournament tournament = getTournament(id);
        tournament.setTitle(newTournamentInfo.getTitle());
        tournament.setDate(newTournamentInfo.getDate());
        return tournaments.save(tournament);
    }

    @Override
    public void deleteTournament(Long id) {
        tournaments.deleteById(id);
    }

    @Override
    public void registerUser(User user, Long id) {
        // TODO TR CODE 1
        // Tournament tournament = getTournament(id);
        // tournament.addUser(user, tournament);
        // tournaments.save(tournament);
    }

    @Override
    public void withdrawUser(User user, Long id) {
        // TODO TR CODE 2
        // Tournament tournament = getTournament(id);

        // // Proceed with the withdrawal
        // handleWithdrawal(user, tournamentData);
        // tournaments.save(tournament);
    }

    @Override
    public void updateResults(Round round) throws MatchNotCompletedException {
        List<Match> matches = round.getMatches();

        // check match ended
        for (Match match : matches) {
            // if match not complete
            if (match.getResult() == 0) {
                // throw exception that it's not complete
                throw new MatchNotCompletedException(match.getId());
            } else {
                // update user data with match results
                double result = match.getResult();
                User black = match.getBlack();
                User white = match.getWhite();
                if (result == -1) {
                    black.setGamePoints(black.getGamePoints() + 1);
                } else if (result == 1) {
                    white.setGamePoints(white.getGamePoints() + 1);
                } else if (result == 0.5) {
                    black.setGamePoints(black.getGamePoints() + 0.5);
                    white.setGamePoints(white.getGamePoints() + 0.5);
                }
            }
        }
        // update match results
    }

    public List<Tournament> getTournamentAboveMin(int ELO) {
        List<Tournament> tournamentList = listTournaments();
        List<Tournament> tournamentListAboveMin = new ArrayList<>();

        for (Tournament tournament : tournamentList) {
            if (tournament.getMinElo() >= ELO) {
                tournamentListAboveMin.add(tournament);
            }
        }

        return tournamentListAboveMin;
    }

    public List<Tournament> getTournamentBelowMax(int ELO) {
        List<Tournament> tournamentList = listTournaments();
        List<Tournament> tournamentListBelowMax = new ArrayList<>();

        for (Tournament tournament : tournamentList) {
            if (tournament.getMaxElo() <= ELO) {
                tournamentListBelowMax.add(tournament);
            }
        }

        return tournamentListBelowMax;
    }

    public List<Tournament> getTournamentAboveMinBelowMax(int minELO, int maxELO) {
        List<Tournament> belowMaxList = getTournamentBelowMax(maxELO);
        List<Tournament> aboveMinList = getTournamentAboveMin(minELO);

        // remove tournaments thats are below min
        belowMaxList.retainAll(aboveMinList);

        return belowMaxList;
    }

    public List<Tournament> getUserEligibleTournament(User user) {
        int userELO = user.getELO();
        List<Tournament> tournamentList = listTournaments();
        List<Tournament> eligibleTournamentList = new ArrayList<>();

        for (Tournament tournament : tournamentList) {
            if (tournament.getMaxElo() >= userELO && tournament.getMinElo() <= userELO) {
                eligibleTournamentList.add(tournament);
            }
        }
        
        return eligibleTournamentList;
    } 

    /**
     * Checks if 2 users have played each other in the tournament before.
     * User order does not matter.
     * If no winner, returns "draw".
     * If never faced each other, returns "no direct encounter".
     * 
     * @param tournament - Tournament to check
     * @param user1    - First user
     * @param user2    - Second user
     * @return Username of winner
     */
    public String directEncounterResultInTournament(Tournament tournament, User user1, User user2) {
        List<Match> directEncounters = matchService.getMatchesBetweenTwoUsers(user1, user2)
                .stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        if (directEncounters.size() == 0) {
            return "no direct encounter";
        }

        Match directEncounter = directEncounters.get(0);

        if (directEncounter.getResult() == -1) {
            return directEncounter.getBlack().getUsername();
        } else if (directEncounter.getResult() == 1) {
            return directEncounter.getWhite().getUsername();
        } else {
            return "draw";
        }
    }

    // Calculate Buchholz score for a user in a specific tournament (sum of
    // opponents' match points)
    public double calculateBuchholzInTournament(User user, Tournament tournament) {
        List<Match> matchList = matchService.getUserMatches(user).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        double buchholzScore = 0;
        for (Match match : matchList) {
            User opponent = match.getWhite().equals(user) ? match.getBlack() : match.getWhite();
            buchholzScore += opponent.getGamePoints();
        }
        return buchholzScore;
    }

    // Calculate Buchholz Cut 1 (exclude lowest opponent score) in a specific
    // tournament
    public double calculateBuchholzCut1InTournament(User user, Tournament tournament) {
        List<Match> matchList = matchService.getUserMatches(user).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        List<Double> opponentScores = new ArrayList<>();
        for (Match match : matchList) {
            User opponent = match.getWhite().equals(user) ? match.getBlack() : match.getWhite();
            opponentScores.add(opponent.getGamePoints());
        }

        if (!opponentScores.isEmpty()) {
            opponentScores.remove(Collections.min(opponentScores));
        }

        return opponentScores.stream().mapToDouble(Double::doubleValue).sum();
    }

    public Round createPairings(Tournament tournament) {
        List<Match> pairings = new ArrayList<>();
        List<User> users = tournament.getUsers();
        Set<User> pairedUsers = new HashSet<>();

        // New round
        Round nextRound = new Round();
        nextRound.setTournament(tournament);

        users.sort(Comparator.comparingDouble(User::getGamePoints).thenComparing(User::getELO).reversed());

        for (int i = 0; i < users.size(); i++) {
            User user1 = users.get(i);

            if (pairedUsers.contains(user1))
                continue;

            // Give priority to user1 (higher ranked) to get opposite colour to prev game
            // assume user1 is white
            boolean isUser1White = isNextColourWhite(user1, tournament);

            for (int j = i + 1; j < users.size(); j++) {
                User user2 = users.get(j);

                if (pairedUsers.contains(user2))
                    continue;

                // check if users have not played each other in tournament
                if (hasPlayedBefore(user1, user2, tournament))
                    continue;

                // check if user2 can be assigned colour
                if (!isColourSuitable(user2, tournament, isUser1White ? "black" : "white"))
                    continue;

                Match newPair = createMatchWithUserColour(user1, isUser1White ? "white" : "black", user2,
                        nextRound);
                pairings.add(newPair);
            }
        }
        // after all users are paired, assign the list to round and return it
        nextRound.setMatches(pairings);
        return nextRound;
    }

    /**
     * Checks previous match to determine next match colour (for colour priority)
     * 
     * @param user     - priority user
     * @param tournament - Tournament matches to consider
     * @return - true if next colour is white
     */
    private boolean isNextColourWhite(User user, Tournament tournament) {
        List<Match> matchList = matchService.getUserMatches(user).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        // Sort in order of increasing round id, since latest round will be largest
        matchList.sort(Comparator.comparing(Match::getId));

        // No games played in tournament yet, default is white
        if (matchList.size() == 0) {
            return true;
        }

        Match previousMatch = matchList.get(matchList.size() - 1);

        // if previous match user is not white, next colour is white, vice versa
        return previousMatch.getWhite().equals(user) ? false : true;
    }

    /**
     * Similar to directEncounterResult. Order of users do not matter
     * 
     * @param user1
     * @param user2
     * @param tournament - Tournament matches to consider
     * @return true if List<match> of matches played size() != 0
     */
    private boolean hasPlayedBefore(User user1, User user2, Tournament tournament) {
        return matchService.getMatchesBetweenTwoUsers(user1, user2).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList())
                .size() == 0;
    }

    /**
     * Checks if user is able to play the nextColour
     * If user hill play same colour three time (including nextColour), returns
     * false
     * If less than 2 games played, returns true
     * 
     * @param user
     * @param tournament - Tournament matches to consider
     * @param nextColour - either "white" or "black" only
     * @return - true if not same colour for 3 times consecutively
     */
    private boolean isColourSuitable(User user, Tournament tournament, String nextColour) {
        List<Match> matchList = matchService.getUserMatches(user).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        // Sort in order of increasing round id, since most recent 2 rounds will be
        // largest
        matchList.sort(Comparator.comparing(Match::getId));

        // If less than 2 games played, can accept any colour
        if (matchList.size() < 2) {
            return true;
        }

        Match mostRecentMatch = matchList.get(matchList.size() - 1);
        Match secondRecentMatch = matchList.get(matchList.size() - 2);

        String mostRecentColour = mostRecentMatch.getWhite().equals(user) ? "white" : "black";
        String secondRecentColour = secondRecentMatch.getWhite().equals(user) ? "white" : "black";

        // If recent colours same as nextColour, user will play the same colour thrice
        boolean sameColourThrice = mostRecentColour.equals(secondRecentColour) && mostRecentColour.equals(nextColour);

        // if playing same colour thrice, it is not suitable
        return !sameColourThrice;
    }

    /**
     * Creates a match object and saves to matchRepo.
     * Assigns users to their respective colours and returns match object.
     * 
     * @param user1
     * @param user1Colour - either "white" or "black" only
     * @param user2
     * @param round
     * @return
     */
    private Match createMatchWithUserColour(User user1, String user1Colour, User user2, Round round) {
        Match match = new Match();
        match.setRound(round);

        if (user1Colour.equals("white")) {
            match.setWhite(user1);
            match.setBlack(user2);
        } else {
            match.setBlack(user1);
            match.setWhite(user2);
        }

        matchService.addMatch(match);
        return match;
    }

    private void handleWithdrawal(User user, Tournament tournament) {
        List<User> userList = tournament.getUsers();
        List<User> waitingList = tournament.getWaitingList();

        LocalDateTime now = LocalDateTime.now();
        if (tournament.getDate() != null && now.isAfter(tournament.getDate().minusDays(1))) {
            User bot = new User();
            bot.setUsername("Bot_" + UUID.randomUUID().toString().substring(0, 5));
            bot.setELO(user.getELO());
            userList.remove(user);
            userList.add(bot);
            tournament.setUsers(userList);

            // Mark the bot to always lose in matches
            for (Round round : tournament.getRounds()) {
                for (Match match : round.getMatches()) {
                    if (match.getWhite().equals(bot)) {
                        match.setResult(-1); // Black wins
                    } else if (match.getBlack().equals(bot)) {
                        match.setResult(1); // White wins
                    }
                }
            }
        } else {
            userList.remove(user);
            if (!waitingList.isEmpty()) {
                userList.add(waitingList.remove(0));
            }
            tournament.setUsers(userList);
            tournament.setWaitingList(waitingList);
        }
    }

    /**
     * Iterates through Users and find the matches played by each user.
     * Calls CalculateElo.update for each user
     * 
     */
    @Override
    public void endTournament(Long id) {
        Tournament tournament = getTournament(id);

        for (User user : tournament.getUsers()) {
            List<Match> userMatches = new ArrayList<>();

            for (Round round : tournament.getRounds()) {
                for (Match match : round.getMatches()) {
                    if (user.equals(match.getWhite()) || user.equals(match.getBlack())) {
                        userMatches.add(match);
                    }
                }
            }

            update(userMatches, user);
        }
    }

    /**
     * Update user ELO via this formula documentation
     * https://en.wikipedia.org/wiki/Chess_rating_system#Linear_approximation
     * 
     * @param matches - List of matches to consider
     * @param user    - User's ELO to update
     * @return none
     */
    public static void update(List<Match> matches, User user) {
        Integer userELO = user.getELO();
        Integer totalDiffRating = 0;
        Integer opponents = 0;
        Integer wins = 0;
        Integer loss = 0;
        Integer developmentCoefficient = 40; // TODO 1: placeholder for now
        Integer classInterval = 200; // TODO 2: placeholder for now

        for (Match match : matches) {
            // void match results as both users didnt play tgt
            if (match.isBYE())
                continue;

            if (match.getWhite().equals(user)) {
                if (match.getResult() == 1)
                    wins++;
                else if (match.getResult() == -1)
                    loss++;

                totalDiffRating += match.getBlack().getELO() - userELO;

            } else {
                if (match.getResult() == 1)
                    loss++;
                else if (match.getResult() == -1)
                    wins++;

                totalDiffRating += match.getWhite().getELO() - userELO;
            }

            opponents++;
        }

        user.setELO(userELO + developmentCoefficient * (wins - loss) / 2
                - (developmentCoefficient / 4 * classInterval) * totalDiffRating);
    }
}
