package mx.com.dezkser.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange((authorize) -> authorize
                        .anyExchange().authenticated()
                )
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        .authenticationSuccessHandler( (webFilterExchange, authentication) -> {
                            // Redirigir a la aplicación Angular después de una autenticación exitosa
                            // Aquí puedes establecer una redirección a tu app Angular
                            webFilterExchange.getExchange().getResponse()
                                    .setStatusCode(HttpStatus.FOUND);
                            webFilterExchange.getExchange().getResponse()
                                    .getHeaders()
                                    .setLocation(URI.create("http://localhost:4200"));
                            return Mono.empty();
                        })
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
