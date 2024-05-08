package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.dto.LoginRequest;

@Service
public class AuthService {

    private static final String FIXED_NAME = "admain";

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String makeAccessKey(LoginRequest request) {
        User user = request.createUser();
        return Jwts.builder()
                .claim("name", FIXED_NAME)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
