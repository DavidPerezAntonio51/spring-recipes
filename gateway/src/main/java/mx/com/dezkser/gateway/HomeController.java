package mx.com.dezkser.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/api/iam")
    public OidcUser getRoles(@AuthenticationPrincipal OidcUser authentication){
        return authentication;
    }

    public record Greeting(String message) {
    }

}
