package roomescape.member.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.TokenResponse;

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
                .setSubject(member.getEmail())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(expiredDateTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new TokenResponse(accessToken);
    }

    public String resolveToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
