package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;

@Component
public class TokenProvider {

    private static final String RANDOM_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(RANDOM_VALUE.getBytes());
    private static final String AUTHENTICATION_PAYLOAD = "name";
    private static final String TOKEN_COOKIE_NAME = "token";
    public static final String AUTHENTICATION_ROLE = "role";

    public String generateTokenOf(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(AUTHENTICATION_PAYLOAD, member.getName())
                .claim(AUTHENTICATION_ROLE, member.getRole())
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

    public Role parseAuthenticationRole(String token) {
        String authenticationInfo = parsePayload(token).get(AUTHENTICATION_ROLE, String.class);
        if (authenticationInfo == null) {
            throw new CustomException(ExceptionCode.NO_AUTHENTICATION_INFO);
        }
        return Role.of(authenticationInfo);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키를 찾을 수 없습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("쿠키를 찾을 수 없습니다."))
                .getValue();
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
