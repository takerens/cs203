    package csd.grp3.tournament;

    import org.springframework.stereotype.Service;
    import csd.grp3.match.Match;
    import csd.grp3.player.Player;
    import csd.grp3.round.Round;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.UUID;

    @Service
    public class PlayerManager {

        // Method to add a player to the tournament
        public void addPlayer(Player player, Tournament tournament) {
            List<Player> playerList = tournament.getPlayers();
            List<Player> waitingList = tournament.getWaitingList();

            if (playerList.size() < tournament.getSize()) {
                playerList.add(player);
            } else {
                waitingList.add(player);
            }
            tournament.setPlayers(playerList);
            tournament.setWaitingList(waitingList);
        }

        // Method to handle the withdrawal of a player from a tournament
        public void handleWithdrawal(Player player, Tournament tournament) {
            List<Player> playerList = tournament.getPlayers();
            List<Player> waitingList = tournament.getWaitingList();

            LocalDateTime now = LocalDateTime.now();
            if (tournament.getDate() != null && now.isAfter(tournament.getDate().minusDays(1))) {
                Player bot = new Player();
                bot.setUsername("Bot_" + UUID.randomUUID().toString().substring(0, 5));
                bot.setELO(player.getELO());
                playerList.remove(player);
                playerList.add(bot);
                tournament.setPlayers(playerList);

                // Mark the bot to always lose in matches
                for (Round round : tournament.getRounds()) {
                    for (Match match : round.getMatches()) {
                        if (match.getWhite().equals(bot)) {
                            match.setResult(-1);  // Black wins
                        } else if (match.getBlack().equals(bot)) {
                            match.setResult(1);  // White wins
                        }
                    }
                }
            } else {
                playerList.remove(player);
                if (!waitingList.isEmpty()) {
                    playerList.add(waitingList.remove(0));
                }
                tournament.setPlayers(playerList);
                tournament.setWaitingList(waitingList);
            }
        }
    }