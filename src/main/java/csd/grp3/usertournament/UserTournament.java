package csd.grp3.usertournament;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference(value = "tournamentUserTournament") // Prevents infinite recursion
    private Tournament tournament;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name="username")
    @JsonBackReference(value = "userUserTournament") // Prevents infinite recursion
    private User user;
    
    // w = waitlist, r = registered, b = bye
    private Character status;

    private double gamePoints = 0;

    private double matchPoints = 0;

    public UserTournament(Tournament tournament, User user, Character status) {
        this.tournament = tournament;
        this.user = user;
        this.status = status;
    }

    public void setStatus(Character status) {
        if (status != null && !status.equals('r') && !status.equals('w') && !status.equals('b')) {
            throw new IllegalArgumentException("Status must be r : registered or w : waiting list or b: bye list");
        }
        this.status = status;
    }

    public double getGamePoints() {
        return gamePoints + matchPoints;
    }
}
