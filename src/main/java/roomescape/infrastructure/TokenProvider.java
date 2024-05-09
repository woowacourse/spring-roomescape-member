package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;

@Component
public class TokenProvider {

    private static final String RANDOM_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(RANDOM_VALUE.getBytes());
    private static final String AUTHENTICATION_PAYLOAD = "name";

    public String generateTokenOf(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(AUTHENTICATION_PAYLOAD, member.getName())
                .claim("role", member.getRole())
                .signWith(SECRET_KEY)
                .compact();
    }

    public String parseAuthenticationInfo(String token) {
        String authenticationInfo = parsePayload(token).get(AUTHENTICATION_PAYLOAD, String.class);
        if (authenticationInfo == null) {
            throw new CustomException(ExceptionCode.NO_AUTHENTICATION_INFO);
        }
        return authenticationInfo;
    }

    public Long parseSubject(String token) {
        String authenticationInfo = parsePayload(token).getSubject();
        if (authenticationInfo == null) {
            throw new CustomException(ExceptionCode.NO_AUTHENTICATION_INFO);
        }
        return Long.parseLong(authenticationInfo);
    }
    private Claims parsePayload(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
