package csd.grp3.tournament;

import csd.grp3.match.Match;
import csd.grp3.match.MatchRepository;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import csd.grp3.usertournament.UserTournament;
import csd.grp3.usertournament.UserTournamentServiceImpl;
import csd.grp3.exception.MatchNotCompletedException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentRepository tournaments;

    @Autowired
    private UserTournamentServiceImpl UTService;

    @Autowired
    private MatchRepository matches;



    public TournamentServiceImpl(TournamentRepository tournaments, UserTournamentServiceImpl UTService) {
        this.tournaments = tournaments;
        this.UTService = UTService;
    }

    @Override
    public List<Tournament> listTournaments() {
        List<Tournament> tournamentList = new ArrayList<>();
        tournaments.findAll().forEach(tournamentList::add);

        if (tournamentList.isEmpty()) {
            return new ArrayList<>();
        }

        return tournamentList;
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        return tournaments.save(tournament);
    }

    // TODO Do this function properly accourding to baseline()
    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        return tournaments.findById(id).map(tournament -> {
            tournament.setTitle(newTournamentInfo.getTitle());
            tournament.setDate(newTournamentInfo.getDate());
            return tournaments.save(tournament);
        }).orElse(null);
    }

    @Override
    public Tournament getTournament(Long id) {
        return tournaments.findById(id).orElse(null);
    }

    @Override
    public void deleteTournament(Long id) {
        tournaments.deleteById(id);
    }

    @Override
    public void registerPlayer(User player, Long id) throws TournamentNotFoundException {
        Optional<Tournament> tournament = tournaments.findById(id);

        if (tournament.isPresent()) {
            List<User> playerList = UTService.getPlayers(id);
            List<User> waitingList = UTService.getWaitingList(id);
            Tournament tournamentData = tournament.get();

            // check if tournament already has that player data
            if (playerList.contains(player) || waitingList.contains(player)) {
                throw new PlayerAlreadyRegisteredException();
            } else {
                // if player isn't inside tournament
                // if tournament is full, we add to waitingList instead
                if (playerList.size() == tournamentData.getSize()) {
                    UTService.add(tournamentData, player, 'w');
                // else, we want to add to normal playerList
                } else {
                    UTService.add(tournamentData, player, 'r');
                }
            }
        } else {
            throw new TournamentNotFoundException(id);
        }
    }


    @Override
    public void withdrawPlayer(User user, Long id) {
        Optional<Tournament> tournament = tournaments.findById(id);
        if (tournament.isPresent()) {
            Tournament tournamentData = tournament.get();
            User playerToWithdraw = null;
    
            // Find the Player object associated with the User
            for (User player : UTService.getPlayers(id)) {
                if (player.equals(user)) {
                    playerToWithdraw = player;
                    break;
                }
            }
    
            // If no matching player is found, throw an exception
            if (playerToWithdraw == null) {
                throw new PlayerNotRegisteredException("Player is not registered in this tournament.");
            }
    
            // Proceed with the withdrawal
            handleWithdrawal(playerToWithdraw, tournamentData);
            tournaments.save(tournamentData);
        } else {
            throw new TournamentNotFoundException(id);
        }
    }

    @Override
    public boolean tournamentExists(Long tournamentId) {
        return tournamentId != null && tournaments.existsById(tournamentId);
    }

    @Override
    public void addRound(Long id) throws TournamentNotFoundException {
        Optional<Tournament> tournament = tournaments.findById(id);

        if (tournament.isPresent()) {
            Tournament tournamentData = tournament.get();
            List<Round> rounds = tournamentData.getRounds();
            rounds.add(createPairings(tournamentData));
            tournaments.save(tournamentData);
        } else {
            throw new TournamentNotFoundException(id);
        }
    }
  
    @Override
    public void updateResults(Round round) throws MatchNotCompletedException {
        List<Match> matches = round.getMatches();
        Tournament tournament = round.getTournament();

        // check match ended
        for (Match match : matches) {
            // if match not complete
            if (match.getResult() == 0) {
                // throw exception that it's not complete
                throw new MatchNotCompletedException(match.getId());
            } else {
                // update player data with match results
                double result = match.getResult();
                User black = match.getBlack();
                User white = match.getWhite();
                if (result == -1) {
                    UTService.updateGamePoints(tournament.getId(), black.getUsername(), 1);
                } else if (result == 1) {
                    UTService.updateGamePoints(tournament.getId(), white.getUsername(), 1);
                } else if (result == 0.5) {
                    UTService.updateGamePoints(tournament.getId(), black.getUsername(), 0.5);
                    UTService.updateGamePoints(tournament.getId(), white.getUsername(), 0.5);
                }
            }
        }
      
        // update match results

        // firstly, we update the round with all new match data.
        round.setMatches(matches);

        // next, we get tournament that the round is in.

        // we get the list of rounds that tournament stores
        List<Round> rounds = tournament.getRounds();

        // now, we find the specific round that we want to update
        Long id = round.getId();
        int index = 0;

        // we loop through each round in tournament round list
        for (Round eachRound : rounds) {
            // if the id of the rounds are the same, we can set it to the new round.
            if (eachRound.getId() == id) {
                // set it using the index we stored.
                rounds.set(index, round);
            }
            // index to find location of round
            index += 1;
        }

        // update tournament with updated list of rounds
        tournament.setRounds(rounds);

        // save tournament data back into database
        tournaments.save(tournament);
    }   

    /**
     * Checks if 2 players have played each other in the tournament before.
     * Player order does not matter.
     * If no winner, returns "draw".
     * If never faced each other, returns "no direct encounter".
     * 
     * @param tournament - Tournament to check
     * @param player1    - First player
     * @param player2    - Second player
     * @return Username of winner
     */
    public String directEncounterResultInTournament(Tournament tournament, User player1, User player2) {
        List<Match> directEncounters = matches.findByBlackAndWhiteOrWhiteAndBlack(player1, player2, player2, player1)
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

    // Calculate Buchholz score for a player in a specific tournament (sum of
    // opponents' match points)
    public double calculateBuchholzInTournament(User player, Tournament tournament) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        double buchholzScore = 0;
        for (Match match : matchList) {
            User opponent = match.getWhite().equals(player) ? match.getBlack() : match.getWhite();
            buchholzScore += UTService.getGamePoints(tournament.getId(), opponent.getUsername());
        }
        return buchholzScore;
    }

    // Calculate Buchholz Cut 1 (exclude lowest opponent score) in a specific
    // tournament
    public double calculateBuchholzCut1InTournament(User player, Tournament tournament) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        List<Double> opponentScores = new ArrayList<>();
        for (Match match : matchList) {
            User opponent = match.getWhite().equals(player) ? match.getBlack() : match.getWhite();
            opponentScores.add(UTService.getGamePoints(tournament.getId(), opponent.getUsername()));
        }

        if (!opponentScores.isEmpty()) {
            opponentScores.remove(Collections.min(opponentScores));
        }

        return opponentScores.stream().mapToDouble(Double::doubleValue).sum();
    }

    public Round createPairings(Tournament tournament) {
        List<Match> pairings = new ArrayList<>();
        List<User> players = UTService.getPlayers(tournament.getId());
        Set<User> pairedPlayers = new HashSet<>();

        // New round
        Round nextRound = new Round();
        nextRound.setTournament(tournament);

        // players.sort(Comparator.comparingDouble(User::getGamePoints).thenComparing(User::getELO).reversed());

        for (int i = 0; i < players.size(); i++) {
            User player1 = players.get(i);

            if (pairedPlayers.contains(player1))
                continue;

            // Give priority to player1 (higher ranked) to get opposite colour to prev game
            // assume player1 is white
            boolean isPlayer1White = isNextColourWhite(player1, tournament);

            for (int j = i + 1; j < players.size(); j++) {
                User player2 = players.get(j);

                if (pairedPlayers.contains(player2))
                    continue;

                // check if players have not played each other in tournament
                if (hasPlayedBefore(player1, player2, tournament))
                    continue;

                // check if player2 can be assigned colour
                if (!isColourSuitable(player2, tournament, isPlayer1White ? "black" : "white"))
                    continue;

                Match newPair = createMatchWithPlayerColour(player1, isPlayer1White ? "white" : "black", player2,
                        nextRound);
                pairings.add(newPair);
            }
        }
        // after all players are paired, assign the list to round and return it
        nextRound.setMatches(pairings);
        return nextRound;
    }

    /**
     * Checks previous match to determine next match colour (for colour priority)
     * 
     * @param player     - priority player
     * @param tournament - Tournament matches to consider
     * @return - true if next colour is white
     */
    private boolean isNextColourWhite(User player, Tournament tournament) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList());

        // Sort in order of increasing round id, since latest round will be largest
        matchList.sort(Comparator.comparing(Match::getId));

        // No games played in tournament yet, default is white
        if (matchList.size() == 0) {
            return true;
        }

        Match previousMatch = matchList.get(matchList.size() - 1);

        // if previous match player is not white, next colour is white, vice versa
        return previousMatch.getWhite().equals(player) ? false : true;
    }

    /**
     * Similar to directEncounterResult. Order of players do not matter
     * 
     * @param player1
     * @param player2
     * @param tournament - Tournament matches to consider
     * @return true if List<match> of matches played size() != 0
     */
    private boolean hasPlayedBefore(User player1, User player2, Tournament tournament) {
        return matches.findByBlackAndWhiteOrWhiteAndBlack(player1, player2, player2, player1).stream()
                .filter(match -> match.getTournament().equals(tournament))
                .collect(Collectors.toList())
                .size() == 0;
    }

    /**
     * Checks if player is able to play the nextColour
     * If player hill play same colour three time (including nextColour), returns
     * false
     * If less than 2 games played, returns true
     * 
     * @param player
     * @param tournament - Tournament matches to consider
     * @param nextColour - either "white" or "black" only
     * @return - true if not same colour for 3 times consecutively
     */
    private boolean isColourSuitable(User player, Tournament tournament, String nextColour) {
        List<Match> matchList = matches.findByBlackOrWhite(player).stream()
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

        String mostRecentColour = mostRecentMatch.getWhite().equals(player) ? "white" : "black";
        String secondRecentColour = secondRecentMatch.getWhite().equals(player) ? "white" : "black";

        // If recent colours same as nextColour, player will play the same colour thrice
        boolean sameColourThrice = mostRecentColour.equals(secondRecentColour) && mostRecentColour.equals(nextColour);

        // if playing same colour thrice, it is not suitable
        return !sameColourThrice;
    }

    /**
     * Creates a match object and saves to matchRepo.
     * Assigns players to their respective colours and returns match object.
     * 
     * @param player1
     * @param player1Colour - either "white" or "black" only
     * @param player2
     * @param round
     * @return
     */
    private Match createMatchWithPlayerColour(User player1, String player1Colour, User player2, Round round) {
        Match match = new Match();
        match.setRound(round);

        if (player1Colour.equals("white")) {
            match.setWhite(player1);
            match.setBlack(player2);
        } else {
            match.setBlack(player1);
            match.setWhite(player2);
        }

        matches.save(match);
        return match;
    }

    private Match handleBYE(User worst, String color, Round round) { // color is color of worst player
        User bot = new User();
        Match match = createMatchWithPlayerColour(worst, color, bot, round);
        match.setBYE(true);
        match.setResult(color.equals("white") ? 1 : -1);
        matches.save(match); // is this necessary
        return match;
    }

    private void handleWithdrawal(User player, Tournament tournament) {
        List<User> playerList = UTService.getPlayers(tournament.getId());
        List<User> waitingList = UTService.getWaitingList(tournament.getId());

        LocalDateTime now = LocalDateTime.now();
        if (tournament.getDate() != null && now.isAfter(tournament.getDate().minusDays(1))) {
            User bot = new User();
            
        } else {
            UTService.delete(tournament.getId(), player.getUsername());
            if (!waitingList.isEmpty()) {
                User addingToPlayer = waitingList.remove(0);
                UTService.updatePlayerStatus(tournament.getId(), addingToPlayer.getUsername(), 'r');
            }
        }
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
}
