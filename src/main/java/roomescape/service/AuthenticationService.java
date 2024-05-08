package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.AuthenticationGenerator;

@Service
public class AuthenticationService {

    private final AuthenticationGenerator authenticationGenerator;

    public AuthenticationService(AuthenticationGenerator authenticationGenerator) {
        this.authenticationGenerator = authenticationGenerator;
    }

    public String createToken(String email) {
        return authenticationGenerator.createToken(email);
    }

    public Cookie createCookie(String token) {
        return authenticationGenerator.createCookie(token);
    }

    public String getPayload(String token) {
        return authenticationGenerator.getPayload(token);
    }
}
