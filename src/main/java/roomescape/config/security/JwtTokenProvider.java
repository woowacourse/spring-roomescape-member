package roomescape.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.exception.TokenInvalidExpiredException;
import roomescape.member.domain.Member;

@Component
@PropertySource("classpath:jwt.properties")
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    public String generateToken(Member member) {
        Date now = new Date();

        return Jwts.builder()
                .claim(JwtClaimKey.MEMBER_ID.getKey(), member.getId().toString())
                .claim(JwtClaimKey.MEMBER_NAME.getKey(), member.getName())
                .claim(JwtClaimKey.ROLE.getKey(), member.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .compact();
    }

    public Long getMemberId(String accessToken) {
        validateAbleToken(accessToken);
        return Long.valueOf((String) tokenToJws(accessToken)
                .getBody()
                .get(JwtClaimKey.MEMBER_ID.getKey()));
    }

    private void validateAbleToken(final String token) {
        Jws<Claims> claims = tokenToJws(token);
        validateExpiredToken(claims);
    }

    private Jws<Claims> tokenToJws(final String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .parseClaimsJws(token);
    }

    private void validateExpiredToken(final Jws<Claims> claims) {
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new TokenInvalidExpiredException("만료시간이 지난 토큰 입니다.");
        }
    }
}
