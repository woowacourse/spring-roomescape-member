package roomescape.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Map;

public class TokenManager {

    // TODO: application.yaml 분리
    private static String secretKey = "나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.";

    public static String generateToken(String subject, Map<String, String> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public static String extractSubject(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public static String extractClaim(String token, String claimName) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get(claimName, String.class);
    }
}
