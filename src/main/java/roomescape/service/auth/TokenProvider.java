package roomescape.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.service.dto.request.LoginRequest;

@Component
public class TokenProvider {

    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateAccessToken(LoginRequest request) {
        return Jwts.builder()
                .claim("name", request.email())
                .claim("role", request.password())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }


}
