package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.domain.LoginMember;

@Component
@PropertySource("classpath:application-secret.properties")
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long expireLength;

    public String createToken(LoginMember loginMember) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setSubject(String.valueOf(loginMember.getId()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
