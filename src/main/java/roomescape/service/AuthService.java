package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.common.exception.UnauthorizedException;
import roomescape.domain.member.Member;

@Service
public class AuthService {
    // !TODO: 배포 이전 SECRET_KEY 하드코딩 방식을 변경하고, KEY 값 변경하기
    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public Cookie generateTokenCookie(Member member) {
        String token = generateToken(member);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public Claims getVerifiedPayload(String token) {
        if (token.isEmpty()) {
            throw new SignatureException("Token is empty");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getVerifiedPayloadFrom(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return getVerifiedPayload(token);
    }

    private String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("토큰을 찾을 수 없습니다: 올바른 쿠키가 아닙니다.");
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new UnauthorizedException("쿠키에서 토큰 정보를 찾을 수 없습니다");
    }
}
