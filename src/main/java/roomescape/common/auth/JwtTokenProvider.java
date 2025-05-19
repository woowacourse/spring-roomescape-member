package roomescape.common.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long getSubjectFromPayloadBy(final String token) {
        validateToken(token);
        return Long.valueOf(getSubject(token));
    }

    private void validateToken(final String token) {
        try {
            getParse().parseClaimsJws(token);
        } catch (final ExpiredJwtException e) {
            throw new InvalidAuthException("유효시간이 만료된 토큰입니다.");
        } catch (final MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            throw new InvalidAuthException("유효하지 않은 토큰입니다.");
        } catch (final IllegalArgumentException e) {
            throw new InvalidAuthException("토큰이 비어있습니다.");
        }
    }

    private String getSubject(final String token) {
        return getParse()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
