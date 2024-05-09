package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import roomescape.domain.User;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;

@Service
public class TokenService {

    private static final String RANDOM_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(RANDOM_VALUE.getBytes());

    public String generateTokenOf(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .signWith(SECRET_KEY)
                .compact();
    }

    public String parseName(String authenticationInfo) {
        String name = parsePayload(authenticationInfo).get("name", String.class);

        if (name == null) {
            throw new CustomException(ExceptionCode.NO_AUTHENTICATION_INFO);
        }

        return name;
    }

    private Claims parsePayload(String authenticationInfo) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(authenticationInfo)
                .getPayload();
    }
}
