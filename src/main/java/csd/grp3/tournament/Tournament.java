package csd.grp3.tournament;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import csd.grp3.user.User;

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
}
