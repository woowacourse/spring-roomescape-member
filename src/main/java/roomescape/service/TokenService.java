package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.User;

@Service
public class TokenService {

    private static final String SECRETKEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateTokenOf(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRETKEY.getBytes()))
                .compact();
    }
}
