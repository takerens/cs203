package csd.grp3.tournament;

import java.time.LocalDateTime;

import csd.grp3.round.Round;
import csd.grp3.usertournament.UserTournament;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToMany(mappedBy = "tournament", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference // Prevents infinite recursion
    private List<Round> rounds = new ArrayList<>();

    @NotNull(message = "Title: not null")
    private String title;

    @NotNull(message = "minElo: put a valid Elo")
    private int minElo;

    @NotNull(message = "maxElo: put a valid Elo")
    private int maxElo;

    @NotNull(message = "Date: put a valid Date")
    private LocalDateTime date;

    @NotNull(message = "size: put a valid tournament size")
    @Min(value = 2, message = "needs at least 2 players")
    private int size;

    @NotNull(message = "totalRounds: put a valid number of rounds")
    @Min(value = 1, message = "tournament should have at least 1 round")
    private int totalRounds;

    private boolean isCalculated = false;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "tournamentUserTournament") // Prevents infinite recursion
    private List<UserTournament> userTournaments = new ArrayList<>();

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;

        // Check if the rounds list exceeds the new totalRounds limit
        if (this.rounds.size() > totalRounds) {
            // Shorten the rounds list to the new totalRounds limit
            this.rounds = new ArrayList<>(this.rounds.subList(0, totalRounds));
        }
    }

    public boolean isOver() {
        return rounds.size() == totalRounds && rounds.get(rounds.size() - 1).isOver();
    }

    public boolean hasStarted() {
        return !(LocalDateTime.now().isBefore(date) || userTournaments.size() < 3); // 1 player is bot, need 2 other players
    }
}