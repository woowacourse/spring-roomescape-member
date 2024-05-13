package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.ui.AuthorizationException;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;


    public String createToken(Member member) {
        Date now = new Date(System.currentTimeMillis());
        Date validity = new Date(System.currentTimeMillis() + VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .claim("id", member.getId())
                .claim("name", member.getMemberName().getValue())
                .claim("email", member.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getPayload(String token, String key) {
        return parseClaims(token)
                .get(key)
                .toString();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException exception) {
            throw new AuthorizationException("토큰이 올바르지 않습니다.");
        }
    }
}
