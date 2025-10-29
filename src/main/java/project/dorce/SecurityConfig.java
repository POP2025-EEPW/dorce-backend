package project.dorce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.dorce.authmanager.AuthFilter;
import project.dorce.usermanager.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;

    public SecurityConfig(UserService userService){
        this.userService = userService;
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/datasets").hasAuthority("MetadataManager")
                        .requestMatchers(HttpMethod.POST, "/api/datasets/*/entries").hasAuthority("MetadataManager")
                        .requestMatchers(HttpMethod.PUT, "/api/datasets/*/entries/*").hasAuthority("MetadataManager")
                        .requestMatchers(HttpMethod.POST, "/api/datasets/*/comments").hasAuthority("MetadataManager")
                        .requestMatchers(HttpMethod.POST, "/api/catalogs").hasAuthority("MetadataManager")
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
