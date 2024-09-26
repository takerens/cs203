package csd.grp3.tournament;

import java.time.LocalDateTime;

import csd.grp3.match.Match;
import csd.grp3.round.Round;
import csd.grp3.user.User;
import jakarta.persistence.*;
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
    private Long id;

    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private List<Round> rounds;

    private String title;
    private int minElo;
    private int maxElo;
    private LocalDateTime date;
    private int size;
    @ManyToMany
    private List<User> waitingList;
    @ManyToMany
    private List<User> participants;
    // private List<Match> matches;


    /**
     * Iterates through Users and find the matches played by each user.
     * Calls CalculateElo.update for each user
     * 
     */
    public void endTournament() {
        for(User user : this.participants) {
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
