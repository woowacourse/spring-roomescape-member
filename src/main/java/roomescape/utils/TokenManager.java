package roomescape.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.exception.EmptyParameterException;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.dto.member.MemberTokenResponse;

@Component
// TODO: 적절한 패키지로 이동
public class TokenManager {
    private static final String AUTH_COOKIE_NAME = "auth_token";

    private final SecretKey secretKey;

    public TokenManager(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public MemberTokenResponse generateToken(Member member) {
        Map<String, ? extends Serializable> claimMap = Map.of(
                "id", member.getId(),
                "name", member.getName()
        );

        String token = Jwts.builder()
                .claims()
                .add(claimMap).and().subject(member.getEmail())
                .signWith(secretKey)
                .compact();
        return new MemberTokenResponse(token);
    }

    public MemberResponse getMemberResponseFromCookies(Cookie[] cookies) {
        String token = CookieParser.searchValueFromKey(cookies, AUTH_COOKIE_NAME);
        return getMemberResponseFromToken(token);
    }

    private MemberResponse getMemberResponseFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new EmptyParameterException("토큰 값이 누락되었습니다.");
        }

        Claims payload = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();

        return new MemberResponse(
                payload.get("id", Long.class),
                payload.get("name", String.class)
        );
    }
}
