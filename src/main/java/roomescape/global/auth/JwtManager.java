package roomescape.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.global.exception.AuthorizationException;

@Component
public class JwtManager {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";

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
            .claim(CLAIM_ROLE, member.getRole())
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public LoginMember findMember(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }
        String token = extractTokenFromCookies(cookies);
        if (token.isEmpty()) {
            throw new AuthorizationException("토큰이 존재하지 않습니다.");
        }
        return parse(token);
    }

    public String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public LoginMember parse(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get(CLAIM_NAME, String.class);
        String email = claims.get(CLAIM_EMAIL, String.class);
        String role = claims.get(CLAIM_ROLE, String.class);

        return new LoginMember(id, email, name, Role.valueOf(role));
    }
}

