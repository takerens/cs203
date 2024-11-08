package csd.grp3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import csd.grp3.jwt.JwtFilter;
import csd.grp3.user.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsSvc) {
        this.userDetailsService = userDetailsSvc;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // React app
        configuration.addAllowedOrigin("http://localhost:80"); // Deployed app
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies/credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this to all routes

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authz) -> authz
                        // Open Access for all
                        .requestMatchers(HttpMethod.POST, "/signup", "/login").permitAll()

                        // Admin-only access
                        .requestMatchers(HttpMethod.GET, "/tournaments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tournaments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/matches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")

                        // User-only access
                        .requestMatchers(HttpMethod.PUT, "/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/tournaments/byElo/*", "/tournaments/byUser/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/tournaments/*/user").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/tournaments/*/user").hasRole("USER")

                        // Access for both User and Admin
                        .requestMatchers(HttpMethod.GET, "/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/tournaments/*", "/tournaments/*/rounds",
                                "/tournaments/*/standings")
                        .hasAnyRole("USER", "ADMIN")

                        // Deny all other requests
                        .anyRequest().denyAll())

                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // .httpBasic(Customizer.withDefaults())
                // .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks
                // .formLogin(form -> form.disable())
                // .headers(header -> header.disable()) // disable the security headers, as we do not return HTML in our
                // // APIs
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
