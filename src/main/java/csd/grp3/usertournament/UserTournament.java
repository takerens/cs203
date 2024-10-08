package csd.grp3.usertournament;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_tournament")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTournament {
    @EmbeddedId
    private UserTournamentId id;

    @ManyToOne
    @MapsId("tournamentId")
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name="username")
    private User user;

    // w = waitlist, r = registered, b = bye
    private char status;

    private double gamePoints;
}
