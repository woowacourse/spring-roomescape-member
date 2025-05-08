package roomescape.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.global.exception.impl.UnauthorizedException;
import roomescape.member.business.domain.Member;

@Component
public class JwtHandler {

    public static final String CLAIM_ID_KEY = "id";
    public static final String CLAIM_ROLE_KEY = "role";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.access.expire-length}")
    private long accessTokenExpireTime;

    public Token createToken(final Member member) {
        Date date = new Date();
        Date accessTokenExpiredAt = new Date(date.getTime() + accessTokenExpireTime);

        String accessToken = Jwts.builder()
                .claim(CLAIM_ID_KEY, member.getId())
                .claim(CLAIM_ROLE_KEY, member.getRole().toString())
                .setIssuedAt(date)
                .setExpiration(accessTokenExpiredAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new Token(accessToken);
    }

    public Map<String, String> decode(String token) {
        Claims claims = parseJwt(token);

        return Map.of(
                CLAIM_ID_KEY, claims.get(CLAIM_ID_KEY).toString(),
                CLAIM_ROLE_KEY, claims.get(CLAIM_ROLE_KEY).toString()
        );
    }

    public String decode(String token, String key) {
        return parseJwt(token)
                .get(key)
                .toString();
    }

    private Claims parseJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException exception) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }
}
