package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;

@Service
public class LoginService {
    String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public Cookie doLogin(LoginRequest request) {
        Member member = new Member("기본", request.email(), request.password());

        String accessToken = Jwts.builder()
                .claim("email", member.getEmail())
                .claim("password", member.getPassword())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String getNameFromCookie(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        // TODO: 쿠키에서 이름 뽑아내는거 해야함.
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .toString();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
