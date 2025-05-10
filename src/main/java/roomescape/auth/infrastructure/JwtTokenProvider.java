package roomescape.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.infrastructure.dto.CredentialDetails;
import roomescape.global.util.NumberParser;
import roomescape.member.domain.Member;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String CLAIM_EMAIL_KEY = "email";
    private static final String CLAIM_ROLE_KEY = "role";
    private static final String CLAIM_NAME_KEY = "name";

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
                .claim(CLAIM_EMAIL_KEY, member.getEmail())
                .claim(CLAIM_ROLE_KEY, member.getRole())
                .claim(CLAIM_NAME_KEY, member.getName())
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
                .id(NumberParser.parseToLong(payload.getSubject()))
                .email(payload.get(CLAIM_EMAIL_KEY, String.class))
                .role(payload.get(CLAIM_ROLE_KEY, String.class))
                .name(payload.get(CLAIM_NAME_KEY, String.class))
                .build();
    }

}
