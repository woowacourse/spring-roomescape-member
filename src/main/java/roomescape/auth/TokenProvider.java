package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
 import roomescape.auth.config.AuthInfo;
import roomescape.member.domain.Member;

@Component
public class TokenProvider {

    private static final String MEMBER_ID_CLAIM = "memberId";

    private final String secretKey;
    private final long validityInMilliseconds;

    public TokenProvider(@Value("${security.jwt.token.secret-key}") final String secretKey,
                         @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim(MEMBER_ID_CLAIM, member.getName())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public AuthInfo extractAuthInfo(final String token) {
        Claims claims = getClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());
        String name = claims.get(MEMBER_ID_CLAIM, String.class);
        return new AuthInfo(memberId, name);
    }

    private Claims getClaims(final String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
