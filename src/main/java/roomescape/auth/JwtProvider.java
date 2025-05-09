package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.entity.LoginMember;

public class JwtProvider {

    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public static String generateToken(LoginMember loginMember) {
        return Jwts.builder()
            .setSubject(loginMember.getId().toString())
            .claim("name", loginMember.getName())
            .claim("role", loginMember.getRole())
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }

    public static Long extractMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());
    }
}
