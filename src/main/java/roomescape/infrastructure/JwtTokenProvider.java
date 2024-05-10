package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.service.exception.AuthorizationException;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(validity)
                .compact();
    }

    public Long getPayload(String token) {
        validateToken(token);
        return Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
    }

    private void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            claims.getBody().getExpiration();
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException("토큰이 만료되었습니다.");
        }
    }
}
