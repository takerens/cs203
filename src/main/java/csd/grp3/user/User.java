package csd.grp3.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import csd.grp3.tournament.Tournament;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "AppUsers")

public class User implements UserDetails{
    /*
     * TODO:
     * Remove elo attribute after creation of player(?)
     */
    private Integer ELO;

    @Id @NotNull(message = "Username should not be null")
    private String username;

    @NotNull (message = "Password should not be null")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotNull(message = "Authorities should not be null")
    private String authorities;

    // Mapping tournament points (Tournament -> Double for points)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // Specify the foreign key in the tournament table
    private Map<Tournament, Integer> gamePoints;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = "ROLE_USER";
    }

    public String getUserRole() {
        return authorities;
    }
  
    // Return a collection of authorities (roles) granted to the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement as needed
    }
}
