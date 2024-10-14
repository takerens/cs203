package csd.grp3.tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.match.Match;
import csd.grp3.match.MatchService;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import csd.grp3.user.UserService;
import csd.grp3.usertournament.UserTournamentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentRepository tournaments;

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserTournamentService UTService;

    @Autowired
    private UserService userService;

    @Override
    public List<Tournament> listTournaments() {
        return tournaments.findAll();
    }

    @Override
    @Transactional
    public Tournament addTournament(Tournament newTournamentInfo) {
        newTournamentInfo.setSize(newTournamentInfo.getSize() + 1);
        tournaments.save(newTournamentInfo);
        registerUser(userService.findByUsername("DEFAULT_BOT"), newTournamentInfo.getId());
        return newTournamentInfo;
    }

    @Override
    public Tournament getTournament(Long id) throws TournamentNotFoundException {
        return tournaments.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
    }

    @Override
    @Transactional
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        Tournament tournament = getTournament(id);
        tournament.setTitle(newTournamentInfo.getTitle());
        tournament.setDate(newTournamentInfo.getDate());
        tournament.setMaxElo(newTournamentInfo.getMaxElo());
        tournament.setMinElo(newTournamentInfo.getMinElo());
        tournament.setSize(newTournamentInfo.getSize());
        tournament.setTotalRounds(newTournamentInfo.getTotalRounds());

        int minElo = tournament.getMinElo();
        int maxElo = tournament.getMaxElo();
        int size = tournament.getSize();

        List<User> users = UTService.getPlayers(id);
        List<User> waitingUsers = UTService.getWaitingList(id);

        // modify list of players based on new Elo limits and new Size limits
        // firstly, we change based on Elo limits
        for (User user : users) {
            if (user.getUsername().equals("DEFAULT_BOT")) {
                continue;
            }

            if (user.getELO() < minElo || user.getELO() > maxElo) {
                UTService.delete(tournament, user);
            }
        }

        // secondly, we change based on size limits
        users = UTService.getPlayers(id); // reinitialize incase of removal due to elo
        int usersSize = users.size();
        if (usersSize > size) {
            for (int i = usersSize - 1; i >= size; i--) {
                if (users.get(i).getUsername().equals("DEFAULT_BOT")) {
                    continue;
                }

                if (users.get(i).getELO() >= minElo && users.get(i).getELO() <= maxElo) { // eligible
                    UTService.updatePlayerStatus(id, users.get(i).getUsername(), 'w');
                } else {
                    UTService.delete(tournament, users.get(i));
                }
            }
        }

        // lastly, we proccess those in waitingList based on new limits imposed
        for (User waitingUser : waitingUsers) {
            if (waitingUser.getELO() >= minElo && waitingUser.getELO() <= maxElo) {
                if (usersSize < size) { // there is still space
                    UTService.updatePlayerStatus(id, waitingUser.getUsername(), 'r');
                    usersSize++;
                } // else leave it be
            } else {
                UTService.delete(tournament, waitingUser); // no longe eligible
            }
        }

        return tournament;
    }

    @Override
    public void deleteTournament(Long id) {
        getTournament(id);
        tournaments.deleteById(id);
    }

    @Override
    @Transactional
    public void registerUser(User tempUser, Long id) throws TournamentNotFoundException {
        Tournament tournament = getTournament(id);
        List<User> userList = UTService.getPlayers(id);
        List<User> waitingList = UTService.getWaitingList(id);
        User user = userService.findByUsername(tempUser.getUsername());

        // check if tournament already has that user data
        if (userList.contains(user) || waitingList.contains(user)) {
            throw new PlayerAlreadyRegisteredException();
        } else { // if user isn't inside tournament
            // if tournament is full, we add to waitingList instead
            if (userList.size() == tournament.getSize()) {
                UTService.add(tournament, user, 'w');
                // else, we want to add to normal userList
            } else {
                UTService.add(tournament, user, 'r');
            }
        }
    }

    @Override
    @Transactional
    public void withdrawUser(User tempUser, Long id) throws UserNotRegisteredException {
        Tournament tournament = getTournament(id);
        List<User> userList = UTService.getPlayers(id);
        List<User> waitingList = UTService.getWaitingList(id);
        User user = userService.findByUsername(tempUser.getUsername());

        if (!userList.contains(user) && !waitingList.contains(user)) {
            throw new UserNotRegisteredException("User has not registered for tournament");
        }

        if (tournament.getDate().isBefore(LocalDateTime.now()) && userList.contains(user)) {
            UTService.updatePlayerStatus(id, user.getUsername(), 'b');
            List<Round> rounds = tournament.getRounds();
            handleBYE(rounds.get(rounds.size() - 1), user); // give opp win for current round
            return;
        }

        UTService.delete(tournament, user); // Remove player

        // handling for before tournament start
        if (tournament.getDate().isAfter(LocalDateTime.now()) && userList.contains(user) && !waitingList.isEmpty()) { // Before and in player list
            User moveUser = waitingList.remove(0);
            UTService.updatePlayerStatus(id, moveUser.getUsername(), 'r');
        }
    }

    @Override
    @Transactional
    public void addRound(Long id) throws TournamentNotFoundException, InvalidTournamentStatus {
        Tournament tournament = getTournament(id);
        int currentRounds = tournament.getRounds().size();

        if (currentRounds > 0) {
            Round lastRound = tournament.getRounds().get(currentRounds - 1);
            updateMatchResults(lastRound); // results for this round only
            if (!lastRound.isOver()) {
                return; // return tournament data as is
            }
            updateResults(lastRound); // update tournament gamepoints
        }

        if (tournament.hasStarted()) {
            createPairings(tournament);
        } else {
            throw new InvalidTournamentStatus("Tournament has not started or less than 2 players");
        }
    }

    @Override
    @Transactional
    public void updateMatchResults(Round round) {
        List<Match> matches = round.getMatches();
        Tournament tournament = round.getTournament();

        for (Match match : matches) {
            // if match is complete
            if (match.getResult() != 0) {
                // update user data with match results
                double result = match.getResult();
                User black = match.getBlack();
                User white = match.getWhite();
                if (result == -1) {
                    UTService.updateMatchPoints(tournament.getId(), black.getUsername(), 1.0);
                    UTService.updateMatchPoints(tournament.getId(), white.getUsername(), 0.0);
                } else if (result == 1) {
                    UTService.updateMatchPoints(tournament.getId(), white.getUsername(), 1.0);
                    UTService.updateMatchPoints(tournament.getId(), black.getUsername(), 0.0);
                } else if (result == 0.5) {
                    UTService.updateMatchPoints(tournament.getId(), black.getUsername(), 0.5);
                    UTService.updateMatchPoints(tournament.getId(), white.getUsername(), 0.5);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateResults(Round round) {
        List<Match> matches = round.getMatches();
        Tournament tournament = round.getTournament();

        for (Match match : matches) {
            UTService.updateGamePoints(tournament.getId(), match.getBlack().getUsername());
            UTService.updateGamePoints(tournament.getId(), match.getWhite().getUsername());
        }
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

    @Override
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

    public List<Tournament> getUserEligibleTournament(int userELO) {
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
     * @param user1      - First user
     * @param user2      - Second user
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
            buchholzScore += UTService.getGamePoints(tournament.getId(), opponent.getUsername());
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
            opponentScores.add(UTService.getGamePoints(tournament.getId(), opponent.getUsername()));
        }

        if (!opponentScores.isEmpty()) {
            opponentScores.remove(Collections.min(opponentScores));
        }

        return opponentScores.stream().mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public List<User> getSortedUsers(Long id) {
        Tournament tournament = getTournament(id);
        List<User> users = UTService.getPlayers(tournament.getId());
        users.sort(Comparator
                .comparingDouble((User user) -> UTService.getGamePoints(tournament.getId(), user.getUsername()))
                .thenComparing(User::getELO)
                .reversed());
        return users;
    }

    @Transactional
    public void createPairings(Tournament tournament) {
        Set<User> pairedUsers = new HashSet<>();

        // New round
        Round nextRound = new Round();
        nextRound.setTournament(tournament);
        tournament.getRounds().add(nextRound);
        List<Match> matches = nextRound.getMatches();

        List<User> users = getSortedUsers(tournament.getId());

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
                newPair.setRound(nextRound);
                matches.add(newPair);
                pairedUsers.add(user1);
                pairedUsers.add(user2);
                break;
            }
        }
        // after all users are paired, assign the list to round and return it
        // nextRound.setMatches(pairings);
        // return nextRound;
    }

    /**
     * Checks previous match to determine next match colour (for colour priority)
     * 
     * @param user       - priority user
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
                .size() != 0;
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

    /**
     * Iterates through Users and find the matches played by each user.
     * Calls CalculateElo.update for each user
     * 
     */
    @Override
    public void endTournament(Long id) {
        Tournament tournament = getTournament(id);
        tournament.setCalculated(true);
        tournaments.save(tournament); // set Calculated

        for (User user : UTService.getPlayers(id)) {
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

    @Transactional
    private void handleBYE(Round round, User user) { // color is color of worst player
        List<Match> matches = round.getMatches();
        for (Match match : matches) {
            if (match.getBlack().equals(user) || match.getWhite().equals(user)) {
                User opponent = match.getBlack().equals(user) ? match.getWhite() : match.getBlack();
                if (opponent.getUsername().equals("DEFAULT_BOT")) {
                    matches.remove(match);
                    break;
                }
                String colorWin = match.getBlack().equals(user) ? "white" : "black";
                match.setResult(colorWin.equals("white") ? 1 : -1);
                match.setBYE(true);
                break;
            }
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
    public void update(List<Match> matches, User user) {
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
        
        userService.updateELO(user, userELO + developmentCoefficient * (wins - loss) / 2
                - (developmentCoefficient / 4 * classInterval) * totalDiffRating);
    }
}
