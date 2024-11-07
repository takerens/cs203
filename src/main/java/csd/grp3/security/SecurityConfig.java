package csd.grp3.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc) {
        this.userDetailsService = userSvc;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests((authz) -> authz
                        // Open Access for all
                        .requestMatchers(HttpMethod.POST, "/signup", "/login").permitAll()

                        // Admin-only access
                        .requestMatchers(HttpMethod.GET, "/tournament").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tournaments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/matches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")

                        // User-only access
                        .requestMatchers(HttpMethod.PUT, "/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/tournaments/byElo", "/tournament/byUser").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/tournaments/*/user").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/*/user").hasRole("USER")

                        // Access for both User and Admin
                        .requestMatchers(HttpMethod.GET, "/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tournaments/*", "/tournaments/*/rounds", "/tournaments/*/standings")
                        .hasAnyRole("USER", "ADMIN")

                        // Deny all other requests
                        .anyRequest().denyAll())

                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks
                .formLogin(form -> form.disable())
                .headers(header -> header.disable()) // disable the security headers, as we do not return HTML in our
                                                     // APIs
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
