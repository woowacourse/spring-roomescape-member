package roomescape.global.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.domain.member.model.Role;
import roomescape.global.exception.AuthorizedException;
import roomescape.global.exception.ErrorMessage;

@Component
public class JwtProvider {

    /**
     * 배포 시 properties로 이동 및 다른 Secret으로 변경할 예정
     */
    private static final String SECRET_KEY = "abcde111abcde111abcde111abcde111";
    private static final long validityInMilliseconds = 900_000;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(JwtRequest jwtRequest) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
            .subject(String.valueOf(jwtRequest.id()))
            .claim("name", jwtRequest.name())
            .claim("role", jwtRequest.role())
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey, SIG.HS256)
            .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizedException(ErrorMessage.INVALID_TOKEN);
        }
    }

    public Role getMemberRole(String token) {
        Claims claims = validateToken(token);
        String role = (String) claims.get("role");
        return Role.valueOf(role);
    }
}
