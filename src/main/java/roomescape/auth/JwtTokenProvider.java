package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.web.exception.AuthenticationException;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration-minutes}")
    private long tokenExpirationMilliseconds;

    public String createToken(Member findMember) {
        Date now = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + tokenExpirationMilliseconds);

        return Jwts.builder()
                .setSubject(findMember.getId().toString())
                .claim("role", findMember.getRole())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getTokenValue(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("로그인된 회원 정보가 없습니다."));
    }

    public String getTokenSubject(String token) {
        validateEmptyToken(token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenRole(String token) {
        validateEmptyToken(token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role").toString();
    }

    private void validateEmptyToken(String token) {
        if (token.isBlank()) {
            throw new AuthenticationException("인증 정보가 없습니다.");
        }
    }
}
