package roomescape.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;
import roomescape.common.exception.AuthorizationException;
import roomescape.member.model.Member;

public class JwtTokenHelper {

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenHelper(final String secretKey, final long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim("memberId", member.getId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractTokenFromCookies(final Cookie[] cookies) {
        final String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("토큰을 찾지 못했습니다"))
                .getValue();

        validateToken(token);
        return token;
    }

    public <T> T getPayloadClaimFromToken(final String token, String claimName, Class<T> returnType) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(claimName, returnType);
    }

    private void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            boolean isBefore = claims.getBody().getExpiration().before(new Date());
            if (isBefore) {
                throw new AuthorizationException("토큰이 만료되었습니다.");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException("토큰이 유효하지 않습니다.");
        }
    }
}
