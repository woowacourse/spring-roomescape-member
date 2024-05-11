package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.service.exception.UnauthorizedException;

@Service
public class JwtService {

    private static final String SECRET_KEY = "011070243be2a70035923d1cf8b65cf39a32a4eb8ae053f31b0f157d0a45bfa8";

    public String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole().name())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnauthorizedException("권한이 없습니다. 로그인을 다시 시도해주세요.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("권한이 없습니다. 로그인을 다시 시도해주세요."))
                .getValue();
    }

    public Claims verifyToken(String token) {
        try {
            return parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("기한이 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new JwtException("JWT 토큰 구성이 올바르지 않습니다.");
        } catch (SignatureException e) {
            throw new JwtException("JWT 토큰 검증에 실패하였습니다.");
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
