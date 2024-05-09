package roomescape.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.user.dto.LoginRequest;
import roomescape.user.dto.LoginResponse;

@Service
public class AuthService {

    private static final String FIXED_NAME = "admin";

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String makeAccessKey(LoginRequest request) {
        return Jwts.builder()
                .claim("name", FIXED_NAME)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginResponse findUser(String token) {
        String name = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);

        return new LoginResponse(name);
    }
}
