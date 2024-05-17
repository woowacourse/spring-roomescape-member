package roomescape.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;
import roomescape.global.exception.ApplicationException;
import roomescape.global.exception.ExceptionType;

public class TokenManager {

    public static final String TOKEN_NAME = "token";
    private static final String SECRET_KEY = "b2cfed4c332ee2838d39a28310c9dbf1d8d43e2d33e941a98461b8dd5e177cbfcb699cf74863f3a7047d56ef00a2b2679e901a051da0334b25d01a97dc348bfc";

    public static String generateToken(String subject, Map<String, String> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public static String extractTokenFrom(Cookie[] cookies) {
        if (cookies == null) {
            throw new ApplicationException(ExceptionType.NO_COOKIE_EXIST);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TokenManager.TOKEN_NAME))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ExceptionType.NO_TOKEN_EXIST))
                .getValue();
    }

    public static String extractSubject(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public static String extractClaim(String token, String claimName) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get(claimName, String.class);
    }
}
