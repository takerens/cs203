package csd.grp3.tournament;

import java.time.LocalDateTime;

import csd.grp3.round.Round;
import csd.grp3.usertournament.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "tournament", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference // Prevents infinite recursion
    private List<Round> rounds = new ArrayList<>();

    @NotNull(message = "Title: not null")
    private String title;

    @NotNull(message = "minElo: put a valid Elo")
    private int minElo;

    @NotNull(message = "maxElo: put a valid Elo")
    private int maxElo;

    private LocalDateTime date;

    @NotNull(message = "size: put a valid tournament size")
    private int size;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "tournamentUserTournament") // Prevents infinite recursion
    private List<UserTournament> userTournaments;
}
