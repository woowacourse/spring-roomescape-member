package roomescape.auth.presentation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.presentation.dto.TokenResponse;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validTimeInMilliSeconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") long validTimeInMilliSeconds) {
        this.secretKey = secretKey;
        this.validTimeInMilliSeconds = validTimeInMilliSeconds;
    }

    public TokenResponse createToken(Member member) {
        Date now = new Date();
        Date expiredDateTime = new Date(now.getTime() + validTimeInMilliSeconds);

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiredDateTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new TokenResponse(accessToken);
    }

    public Long resolveTokenToMemberId(String token) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public boolean validateTokenExpiration(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
