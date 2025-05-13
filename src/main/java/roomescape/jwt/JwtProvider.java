package roomescape.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.MemberRoleType;

@Component
public class JwtProvider {

    private static final String ISSUER = "roomescape";
    private static final Duration ACCESS_EXPIRATION = Duration.ofDays(1);

    private final SecretKey secretKey;

    public JwtProvider(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(JwtRequest jwtRequest) {
        return Jwts.builder()
                .subject(Long.toString(jwtRequest.id()))
                .claim("name", jwtRequest.name())
                .claim("role", jwtRequest.role())
                .issuer(ISSUER)
                .issuedAt(jwtRequest.issuedAt())
                .expiration(new Date(jwtRequest.issuedAt().getTime() + ACCESS_EXPIRATION.toMillis()))
                .signWith(secretKey, SIG.HS256)
                .compact();
    }

    public JwtRequest verifyToken(String jwtToken) {

        if (jwtToken == null) {
            throw new JwtException("유효하지 않은 jwt 토큰값입니다");
        }

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(ISSUER)
                    .build()
                    .parseSignedClaims(jwtToken);
            return new JwtRequest(Long.parseLong(claimsJws.getPayload().getSubject()),
                    claimsJws.getPayload().get("name", String.class),
                    MemberRoleType.from(claimsJws.getPayload().get("role", String.class)),
                    claimsJws.getPayload().getIssuedAt());
        } catch (Exception e) {
            throw new JwtException("jwt 검증에 문제가 발생했습니다");
        }
    }
}
