package roomescape.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider implements AuthTokenProvider {

    private final Algorithm algorithm;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") long validityInMilliseconds
    ) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public String createTokenFromMember(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);

        return JWT.create()
                .withSubject(String.valueOf(member.getId()))
                .withClaim("name", member.getName())
                .withClaim("role", member.getRole().name())
                .withExpiresAt(expiry)
                .sign(algorithm);
    }
}
