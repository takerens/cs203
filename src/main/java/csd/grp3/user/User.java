package csd.grp3.user;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @Id @NotNull(message = "Username should not be null")
    private String username;

    @NotNull(message = "Password should not be null")
    private String password;

    @NotNull(message = "Authorities should not be null")
    private String authorities;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = "ROLE_USER";
    }
  
    // Return a collection of authorities (roles) granted to the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }
}
