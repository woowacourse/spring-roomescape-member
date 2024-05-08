package roomescape.auth;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Date;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenGenerator {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final long expirationTime;
    private final String secretKey;
    private final JwtParser jwtParser;

    public JwtTokenGenerator(
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
                        .claim("name", member.getName())
                        .claim("email", member.getEmail())
                        .signWith(SIGNATURE_ALGORITHM, secretKey)
                        .compact()
        );
    }

    public Long getMemberId(Token token) {
        return Long.valueOf(jwtParser
                .parseClaimsJws(token.getToken())
                .getBody()
                .getSubject()
        );
    }
}
