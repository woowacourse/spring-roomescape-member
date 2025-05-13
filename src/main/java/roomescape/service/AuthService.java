package roomescape.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.support.auth.AuthorizationExtractor;
import roomescape.support.auth.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthService(JwtTokenProvider jwtTokenProvider, AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = authorizationExtractor;
    }

    public String extractToken(final HttpServletRequest request) {
        return authorizationExtractor.extract(request);
    }

    public String createToken(final String email) {
        return jwtTokenProvider.createToken(email);
    }

    public String getPayload(final String token) {
        return jwtTokenProvider.getPayload(token);
    }

    public boolean validateToken(final String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
