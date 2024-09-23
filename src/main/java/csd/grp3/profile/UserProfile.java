package csd.grp3.profile;

import java.util.ArrayList;
import java.util.List;

import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="UserProfiles")
@Getter
@Setter

public class UserProfile {

    @Id
    @NotNull(message = "profile must have a user tagged to it")
    private User profileOwner;

    @NotNull(message = "profile must have a elo value")
    private int elo;

    private String displayName;
    private List<Tournament> history;
    private List<Tournament> registered;

    public UserProfile(User profileOwner) {
        this.profileOwner = profileOwner;
        this.elo = 100;
        this.history = new ArrayList<>();
        this.registered = new ArrayList<>();
    }
}
