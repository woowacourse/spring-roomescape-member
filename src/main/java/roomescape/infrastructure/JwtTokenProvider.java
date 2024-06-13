package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.global.exception.UnAuthorizedException;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    public static final String TOKEN = "token=";

    public String createToken(Member member) {
        Date now = new Date(System.currentTimeMillis());
        Date validity = new Date(System.currentTimeMillis() + VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .claim("id", member.getId())
                .claim("name", member.getName().getValue())
                .claim("email", member.getEmail())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getPayload(String token, String key) {
        String rawToken = removePrefix(token);
        Object claims = parseClaims(rawToken).get(key);

        return claims.toString();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException exception) {
            throw new UnAuthorizedException("토큰이 올바르지 않습니다.");
        }
    }

    private String removePrefix(String token) {
        if (token.startsWith(TOKEN)) {
            return token.substring(TOKEN.length());
        } else {
            return token;
        }
    }
}
