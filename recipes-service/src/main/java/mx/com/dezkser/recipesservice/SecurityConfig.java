package mx.com.dezkser.recipesservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/recipes/**").hasAnyAuthority("SCOPE_recipes:read")
                        .requestMatchers(HttpMethod.POST, "/recipes/**").hasAnyAuthority("SCOPE_recipes:writer")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((jwt) -> jwt
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}
