package roomescape.auth;

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
import roomescape.domain.member.Member;
import roomescape.exception.LoginException;
import roomescape.service.member.dto.MemberResponse;
import roomescape.service.member.dto.MemberTokenResponse;
import roomescape.utils.CookieParser;

@Component
public class MemberTokenConverter {
    public static final String AUTH_COOKIE_NAME = "auth_token";

    private final SecretKey secretKey;

    public MemberTokenConverter(@Value("${jwt.secret}") String secret) {
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
        String token = CookieParser.searchValueFromKey(cookies, AUTH_COOKIE_NAME)
                .orElseThrow(() -> new LoginException("로그인 정보가 존재하지 않습니다."));
        return getMemberResponseFromToken(token);
    }

    private MemberResponse getMemberResponseFromToken(String token) {
        Claims payload = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();

        return new MemberResponse(
                payload.get("id", Long.class),
                payload.get("name", String.class)
        );
    }
}
