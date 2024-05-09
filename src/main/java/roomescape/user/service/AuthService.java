package roomescape.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.user.dto.AccessToken;
import roomescape.user.dto.LoginRequest;

@Service
public class AuthService {

    private static final String FIXED_NAME = "admin";

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String makeAccessKey(LoginRequest request) {
        return Jwts.builder()
                .claims()
                .add("email", request.email())
                .add("name", FIXED_NAME)
                .and()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AccessToken decode(String token) {
        String email = getPayload(token).get("email", String.class);
        String name = getPayload(token).get("name", String.class);
        return new AccessToken(email, name);
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
