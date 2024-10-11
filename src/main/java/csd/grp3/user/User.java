package csd.grp3.user;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import csd.grp3.usertournament.UserTournament;
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
    private Integer ELO;

    @Id @NotNull(message = "Username should not be null")
    private String username;

    @NotNull (message = "Password should not be null")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotNull(message = "Authorities should not be null")
    private String authorities;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = "ROLE_USER";
        this.ELO = 100; // Default
    }

    public User(String username, String password, String authorities, int ELO) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.ELO = ELO;
    }

    public String getUserRole() {
        return authorities;
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<UserTournament> userTournaments = new ArrayList<>();
  
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
