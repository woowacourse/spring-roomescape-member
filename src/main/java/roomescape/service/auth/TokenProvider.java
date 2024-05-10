package roomescape.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.exception.AuthenticationException;

@Component
public class TokenProvider {

    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateAccessToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long parseToken(String token) {
        if(token == "") {
            throw new AuthenticationException("잘못된 토큰 정보입니다.");
        }
        return Long.valueOf(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }
}
