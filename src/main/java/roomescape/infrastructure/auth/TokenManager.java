package roomescape.infrastructure.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Date;

@Component
public class TokenManager {
    private final long expirationTime;
    private final String secretKey;
    private final JwtParser jwtParser;

    public TokenManager(
            @Value("${jwt.expirationTime}") long expirationTime,
            @Value("${jwt.secretKey}") String secretKey
    ) {
        this.expirationTime = expirationTime;
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
    }

    public Token generate(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return new Token(
                Jwts.builder()
                        .setHeaderParam("typ", "jwt")
                        .setSubject(member.getId().toString())
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .compact()
        );
    }

    public Long getMemberId(Token token) {
        if (validateToken(token)) {
            return Long.valueOf(jwtParser.parseClaimsJws(token.getToken())
                    .getBody()
                    .getSubject()
            );
        }

        throw new IllegalArgumentException("[ERROR] 만료된 토큰입니다.");
    }

    private boolean validateToken(Token token) {
        try {
            Jws<Claims> claims = jwtParser.parseClaimsJws(token.getToken());
            Date expirationDate = claims.getBody().getExpiration();
            return !expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
