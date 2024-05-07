package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import roomescape.dto.UserPayload;

@Component
public class JwtProvider {

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public String createToken(UserPayload userPayload) {
        return Jwts.builder()
                .setSubject(userPayload.id())
                .claim("name", userPayload.name())
                .claim("email", userPayload.email())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String getSubject(String accessToken) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJwt(accessToken)
                .getBody()
                .getSubject();
    }
}
