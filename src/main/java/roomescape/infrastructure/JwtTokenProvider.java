package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.service.tokenmanager.TokenProvider;

@Component
@PropertySource("classpath:application-secret.properties")
public class JwtTokenProvider implements TokenProvider {
    private final String secretKey;

    private final long expireLength;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                            @Value("${security.jwt.token.expire-length}") long expireLength) {
        this.secretKey = secretKey;
        this.expireLength = expireLength;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
