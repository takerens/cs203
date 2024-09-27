package csd.grp3.profile;

import java.util.ArrayList;
import java.util.List;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @OneToOne // assuming a 1-to-1 relationship with User
    @JoinColumn(name = "user_username")
    @NotNull(message = "profile must have a user tagged to it")
    private User profileOwner;

    @NotNull(message = "profile must have an elo value")
    private int elo;

    private String displayName;

    @OneToMany // assuming a 1-to-many relationship with Tournament
    @JoinColumn(name = "tournament_id")
    private List<Tournament> history = new ArrayList<>();

    @OneToMany // assuming a 1-to-many relationship with Tournament
    @JoinColumn(name = "tournament_id")
    private List<Tournament> registered = new ArrayList<>();

    public Profile(User profileOwner) {
        this.profileOwner = profileOwner;
        this.elo = 100;
    }

    public void addTournamentToHistory(Tournament tournament) {
        this.history.add(tournament);
    }

    public void addTournamentToRegistered(Tournament tournament) {
        this.registered.add(tournament);
    }

    public void removeTournamentFromRegistered(Tournament tournament) {
        this.registered.remove(tournament);
    }
}
