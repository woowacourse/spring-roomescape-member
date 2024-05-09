package roomescape.service.security;

import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.User;
import roomescape.exception.user.AuthenticationFailureException;

public class JwtUtils {
    private static final String SECRET_KEY = "hellowootecoworldhihowareyouiamfinethankyouandyou";

    private JwtUtils() {
        throw new AssertionError("유틸 클래스입니다. 생성할 수 없습니다.");
    }

    public static String encode(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(Map.of("name", user.getName(), "email", user.getEmail()))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public static Long decode(String token) {
        try {
            token = token.replace("token=", "");

            return Long.parseLong(Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }
}
