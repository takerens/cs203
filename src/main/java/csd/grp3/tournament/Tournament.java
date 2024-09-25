package csd.grp3.tournament;

import java.time.LocalDateTime;

import csd.grp3.match.Match;
import csd.grp3.player.Player;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Entity
@Table(name="Tournaments")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private List<Round> rounds;

    @NotNull(message = "Title: not null")
    private String title;

    @NotNull(message = "minElo: put a valid Elo")
    private int minElo;

    @NotNull(message = "maxElo: put a valid Elo")
    private int maxElo;

    private LocalDateTime date;

    @NotNull(message = "size: put a valid tournament size")
    private int size;

    @ManyToMany
    private List<Player> waitingList;
    @ManyToMany
    private List<Player> players;
    // private List<Match> matches;


    /**
     * Iterates through Users and find the matches played by each user.
     * Calls CalculateElo.update for each user
     * 
     */
    public void endTournament() {
        for(User user : this.players) {
            List<Match> userMatches = new ArrayList<>();

            for (Round round : this.rounds) {
                for (Match match : round.getMatches()) {
                    if (user.equals(match.getWhite()) || user.equals(match.getBlack()) ) {
                        userMatches.add(match);
                    }
                }
            }

            CalculateELO.update(userMatches, user);
        }
    }
}
