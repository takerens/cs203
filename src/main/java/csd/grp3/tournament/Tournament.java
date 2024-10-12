package csd.grp3.tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import csd.grp3.round.Round;
import csd.grp3.usertournament.UserTournament;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private LocalDateTime date;

    @NotNull(message = "size: put a valid tournament size")
    private int size;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference(value = "tournamentUserTournament") // Prevents infinite recursion
    private List<UserTournament> userTournaments = new ArrayList<>();
}
