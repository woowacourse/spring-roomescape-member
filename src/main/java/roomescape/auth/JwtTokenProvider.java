package roomescape.auth;

import io.jsonwebtoken.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import roomescape.model.Member;

@ConfigurationProperties(prefix = "token-provider")
@Component
public class JwtTokenProvider {

    private String secretKey;

    public String createToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getMemberId(final String token) {
        final String tokenSubject = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(tokenSubject);
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
