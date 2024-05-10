package roomescape.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_EMAIL = "email";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim(CLAIM_NAME, member.getName())
            .claim(CLAIM_EMAIL, member.getEmail())
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public LoginMember parse(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get(CLAIM_NAME, String.class);
        String email = claims.get(CLAIM_EMAIL, String.class);

        return new LoginMember(id, email, name);
    }
}

