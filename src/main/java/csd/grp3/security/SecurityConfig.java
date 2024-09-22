package csd.grp3.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
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
                            .requestMatchers( "/login", "/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/verify").permitAll()
                            .anyRequest().authenticated()
            )
            // ensure that the application won’t create any session in our stateless REST APIs
            // .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()) // CSRF protection is needed only for browser based attacks
            .formLogin(form -> form.disable())
                    // .loginProcessingUrl("/verify"))
                    // .successForwardUrl("/index"))
            // .exceptionHandling(handling -> handling
            //     .authenticationEntryPoint((request, response, authException) -> {
            //             response.sendRedirect("/login?error=Please Login First");
            //         })
            //     )
            .headers(header -> header.disable()) // disable the security headers, as we do not return HTML in our APIs
            .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
