package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.util.Date;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenProvider {
    @Value("${jwt.expirationTime}")
    private long EXPIRATION_TIME;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
