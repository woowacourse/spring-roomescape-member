package roomescape.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.service.dto.member.MemberTokenResponse;

@Component
// TODO: 적절한 패키지로 이동
public class TokenManager {
    private static final String COOKIE_NAME = "auth_token";

    private final String secretKey;

    public TokenManager(@Value("${jwt.secret") String secret) {
        this.secretKey = secret;
    }

    public MemberTokenResponse generateToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.putAll(Map.of(
                "id", member.getId(),
                "name", member.getName()
        ));

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return new MemberTokenResponse(token);
    }
}
