package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String EMAIL_KEY = "email";
    private static final String ID_KEY = "id";
    private static final String ROLE_KEY = "role";

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expired-period}")
    private long expiredPeriod;

    public String generate(Member member) {
        long now = new Date().getTime();

        return Jwts.builder()
                .claim(ID_KEY, member.getId())
                .claim(EMAIL_KEY, member.getEmail())
                .claim(ROLE_KEY, member.getRole().name())
                .setExpiration(new Date(now + expiredPeriod))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Map<String, String> decode(String token) {
        Map<String, String> decodedClaims = new HashMap<>();

        Claims claims = parseJwt(token);
        decodedClaims.put(EMAIL_KEY, claims.get(EMAIL_KEY).toString());
        decodedClaims.put(ID_KEY, claims.get(ID_KEY).toString());
        decodedClaims.put(ROLE_KEY, claims.get(ROLE_KEY).toString());

        return decodedClaims;
    }

    public String decode(String token, String key) {
        return parseJwt(token)
                .get(key)
                .toString();
    }

    private Claims parseJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException exception) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }
}
