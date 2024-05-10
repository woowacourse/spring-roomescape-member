package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;

@Service
public class JwtService {

    private static final String SECRET_KEY = "011070243be2a70035923d1cf8b65cf39a32a4eb8ae053f31b0f157d0a45bfa8";

    public String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long verifyToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new JwtException("기한이 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new JwtException("JWT 토큰 구성이 올바르지 않습니다.");
        } catch (SignatureException e) {
            throw new JwtException("JWT 토큰 검증에 실패하였습니다.");
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
