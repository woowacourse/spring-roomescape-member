package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.infrastructure.dto.CredentialDetails;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.expiration}")
    int expiration;
    @Value("${jwt.issure}")
    String issure;

    @Override
    public String create(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .claim("name", member.getName())
                .issuer(issure)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Override
    public CredentialDetails extractToCredentialDetails(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return CredentialDetails.builder()
                .id(Long.valueOf(payload.getId()))
                .email(payload.get("email", String.class))
                .role(payload.get("role", String.class))
                .name(payload.get("name", String.class))
                .build();
    }

}
