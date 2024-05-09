package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import roomescape.dto.member.MemberPayload;

@Component
public class JwtProvider {

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public String createToken(MemberPayload memberPayload) {

        return Jwts.builder()
                .setSubject(memberPayload.id())
                .claim("name", memberPayload.name())
                .claim("email", memberPayload.email())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String getSubject(String accessToken) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}
