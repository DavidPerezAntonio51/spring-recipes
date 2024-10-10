package mx.com.dezkser.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.netty.http.server.HttpServerResponse;

@RestController
public class HomeController {

    @GetMapping("/")
    public Greeting hello(Authentication authentication) {
        return new Greeting("Hello, " + authentication.getName());
    }

    public record Greeting(String message) {
    }

}
