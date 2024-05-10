package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private static final String COOKIE_NAME = "token";
    private static final String secretKey = "secret-key-of-spring-roomescape-member";
    private static final long validityInMilliseconds = 360 * 1000L;

    public String createToken(final String payload, final String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(final String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenFromCookies(final HttpServletRequest request) {
        validateRequestCookies(request);
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
    }

    private void validateRequestCookies(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new JwtException("로그인이 필요합니다.");
        }
    }

    public void validateTokenExpiration(final String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("로그인 기간이 만료되었습니다. 다시 로그인 해주세요.");
        }
    }

    public void validateTokenRole(final String token) {
        final Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        final boolean isNotAdmin = !claims.getBody().get("role").equals("ADMIN");

        if (isNotAdmin) {
            throw new JwtException("권한이 없습니다. 관리자 권한이 필요합니다.");
        }
    }
}
