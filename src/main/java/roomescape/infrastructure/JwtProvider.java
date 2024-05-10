package roomescape.infrastructure;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.dto.member.MemberPayload;
import roomescape.util.DateUtil;

@Component
public class JwtProvider {

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public String createToken(MemberPayload memberPayload) {

        return Jwts.builder()
                .setSubject(memberPayload.id())
                .setIssuedAt(DateUtil.getCurrentTime())
                .setExpiration(DateUtil.getAfterTenMinutes())
                .claim("name", memberPayload.name())
                .claim("email", memberPayload.email())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJwt(token)
                    .getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
