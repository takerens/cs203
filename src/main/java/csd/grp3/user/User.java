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
import com.fasterxml.jackson.annotation.JsonManagedReference;

import csd.grp3.usertournament.UserTournament;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    private Integer ELO = 100;

    @Id @NotNull(message = "Username should not be null")
    private String username;

    @NotNull (message = "Password should not be null")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    // @JsonIgnore
    private String password;

    @NotNull(message = "Authorities should not be null")
    // @JsonIgnore
    private String authorities;

    // @OneToMany(mappedBy = "user",   fetch = FetchType.EAGER) // cascade = CascadeType.ALL,orphanRemoval = true,
    // @JsonManagedReference(value = "userUserTournament") // Prevents infinite recursion
    // private List<UserTournament> userTournaments = new ArrayList<>();

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

    // @JsonIgnore
    public String getUserRole() {
        return authorities;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference(value = "userUserTournament") // Prevents infinite recursion
    private List<UserTournament> userTournaments = new ArrayList<>();
  
    // Return a collection of authorities (roles) granted to the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true; // Implement as needed
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true; // Implement as needed
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true; // Implement as needed
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true; // Implement as needed
    }

    
}
