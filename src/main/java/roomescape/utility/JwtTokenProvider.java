package roomescape.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.MemberRole;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.exception.UnauthorizedException;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}")
            String secretKey,
            @Value("${security.jwt.token.expire-length}")
            long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String makeAccessToken(long id, String name, MemberRole role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("name", name)
                .claim("role", role)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public AuthenticationInformation parseToken(String token) {
        validateToken(token);
        Claims tokenBody = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Long id = Long.valueOf(tokenBody.getSubject());
        String name = String.valueOf(tokenBody.get("name"));
        MemberRole role = MemberRole.valueOf(String.valueOf(tokenBody.get("role")));
        return new AuthenticationInformation(id, name, role);
    }

    private void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("[ERROR] 유효하지 않은 인증정보입니다.");
        }
    }
}
