package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.dto.MemberResponse;
import roomescape.model.Member;

@Service
public class AuthenticationService {
    public static String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";


    public String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getMemberName().getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

    }

    public MemberResponse validateTokenAndGetName(String accessToken) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            String name = payload.get("name", String.class);
            return new MemberResponse(name);
        } catch (JwtException e) {
            throw new IllegalArgumentException("접근할 수 없습니다.");
        }
    }

    public Long validateTokenAndGetId(String accessToken) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            return Long.parseLong(payload.getSubject());
        } catch (JwtException e) {
            throw new IllegalArgumentException("접근할 수 없습니다.");
        }
    }
}
