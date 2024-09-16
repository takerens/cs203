package services.tournament.src.main.java.com.cs203proj.tournament.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Tournaments")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int minElo;
    private int maxElo;
    private LocalDateTime date;
    // private List<User> participants;
    // private List<Match> matches;
}
