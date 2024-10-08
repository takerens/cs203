package csd.grp3.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;

import csd.grp3.usertournament.UserTournament;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserTournament> userTournaments = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = "ROLE_USER";
        this.ELO = 100;
    }

    public String getUserRole() {
        return authorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check for reference equality
        if (obj == null || getClass() != obj.getClass()) return false; // Check for null and class match
        User user = (User) obj; // Cast to User
        return username.equals(user.username); // Compare usernames
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
