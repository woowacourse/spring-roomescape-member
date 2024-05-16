package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.AuthorizationException;

@Component
public class JwtTokenHandler {
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenHandler(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole().getName())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Member getMember(String token) {
        try {
            Long id = Long.parseLong(getBody(token)
                    .getSubject());
            String name = getBody(token)
                    .get("name", String.class);
            Role role = Role.of(getBody(token)
                    .get("role", String.class));

            return new Member(id, name, role);
        } catch (ExpiredJwtException exception) {
            throw new AuthorizationException("인증이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException exception) {
            throw new AuthorizationException("인증 정보를 확인할 수 없습니다.");
        }
    }

    private Claims getBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
