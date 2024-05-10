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

    public static final String EMAIL_KEY = "email";
    public static final String ID_KEY = "id";
    private static final long EXPIRED_PERIOD = (long) 1000 * 60 * 60; // 30일

    @Value("${jwt.secret")
    private String jwtSecret;

    public String generate(Member member) {
        long now = new Date().getTime();
        
        return Jwts.builder()
                .claim(ID_KEY, member.getId())
                .claim(EMAIL_KEY, member.getEmail())
                .setExpiration(new Date(now + EXPIRED_PERIOD))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Map<String, String> decode(String token) {
        try {
            Map<String, String> decodedClaims = new HashMap<>();

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            decodedClaims.put(EMAIL_KEY, claims.get(EMAIL_KEY).toString());
            decodedClaims.put(ID_KEY, claims.get(ID_KEY).toString());

            return decodedClaims;
        } catch (SignatureException exception) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }

    public String decode(String token, String key) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .get(key)
                    .toString();
        } catch (SignatureException exception) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }
}
