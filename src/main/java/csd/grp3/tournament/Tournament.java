package csd.grp3.tournament;

import java.time.LocalDateTime;

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
    private Long id;

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
    private List<User> waitingList;
    @ManyToMany
    private List<User> users;
    // private List<Match> matches;
}
