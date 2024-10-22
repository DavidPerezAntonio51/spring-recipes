package mx.com.dezkser.gateway;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayReactiveOAuth2AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@Configuration
@EnableWebFluxSecurity
@EnableAutoConfiguration(exclude = GatewayReactiveOAuth2AutoConfiguration.class)
public class SecurityConfig {
    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/angular-ui/**","/assets/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                            // Redirigir a la aplicación Angular después de una autenticación exitosa
                            // Aquí puedes establecer una redirección a tu app Angular
                            webFilterExchange.getExchange().getResponse()
                                    .setStatusCode(HttpStatus.FOUND);
                            webFilterExchange.getExchange().getResponse()
                                    .getHeaders()
                                    .setLocation(URI.create("http://localhost:8080/angular-ui/authorized"));
                            return Mono.empty();
                        })
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/gateway-client"))
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public TokenExchangeReactiveOAuth2AuthorizedClientProvider tokenExchange(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        Function<OAuth2AuthorizationContext, Mono<OAuth2Token>> subjectTokenResolver =
                createTokenResolver(authorizedClientManager, "gateway-client");
        TokenExchangeReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                new TokenExchangeReactiveOAuth2AuthorizedClientProvider();
        authorizedClientProvider.setSubjectTokenResolver(subjectTokenResolver);
        return authorizedClientProvider;
    }

    private static Function<OAuth2AuthorizationContext, Mono<OAuth2Token>> createTokenResolver(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager, String clientRegistrationId) {
        return (context) -> {
            OAuth2AuthorizeRequest authorizeRequest =
                    OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                            .principal(context.getPrincipal())
                            .build();
            return authorizedClientManager.authorize(authorizeRequest)
                    .map(OAuth2AuthorizedClient::getAccessToken);
        };
    }
}
