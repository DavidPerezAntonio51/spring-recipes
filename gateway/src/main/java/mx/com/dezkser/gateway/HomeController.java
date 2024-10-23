package mx.com.dezkser.gateway;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @GetMapping("/api/iam")
    public List<String> getRoles(@AuthenticationPrincipal OidcUser authentication){
        return authentication.getClaimAsStringList("roles");
    }

    public record Greeting(String message) {
    }

}
