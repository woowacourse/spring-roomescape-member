package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.AuthInfo;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;

@Component
public class TokenProvider {

    private static final String MEMBER_ID_CLAIM = "memberId";
    private static final String MEMBER_ROLE_CLAIM = "memberRole";

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
                .claim(MEMBER_ROLE_CLAIM, member.getMemberRole())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public AuthInfo extractAuthInfo(final String token) {
        Claims claims = getClaims(token);
        return new AuthInfo(
                Long.parseLong(claims.getSubject()),
                claims.get(MEMBER_ID_CLAIM, String.class),
                MemberRole.valueOf(claims.get(MEMBER_ROLE_CLAIM, String.class)));
    }

    private Claims getClaims(final String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
