package csd.grp3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply to all routes
            .allowedOrigins("http://localhost:80", "http://localhost:3000") // React & Deployed Origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP Methods
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true);
    }

    
}
