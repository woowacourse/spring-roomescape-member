package roomescape.application.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public static String createToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public static String checkByToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }
    }
}
