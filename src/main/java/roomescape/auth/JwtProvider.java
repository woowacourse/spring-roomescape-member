package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.dto.TokenInfo;
import roomescape.dto.request.LoginMember;
import roomescape.global.exception.AuthenticationException;

@Service
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(LoginMember member) {
        return Jwts.builder()
                .subject(member.id().toString())
                .claim("role", member.role().toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

    }

    public TokenInfo verifyTokenAndExtractInfo(String accessToken) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            return new TokenInfo(Long.parseLong(payload.getSubject()), payload.get("role", String.class));
        } catch (JwtException e) {
            throw new AuthenticationException("접근할 수 없습니다.");
        }
    }
}
