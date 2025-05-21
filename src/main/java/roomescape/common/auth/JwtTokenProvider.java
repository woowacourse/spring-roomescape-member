package roomescape.common.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.auth.InvalidTokenException;
import roomescape.domain.member.Member;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") final String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(final Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(secretKey)
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
            throw new InvalidTokenException("유효시간이 만료된 토큰입니다.");
        } catch (final MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        } catch (final IllegalArgumentException e) {
            throw new InvalidTokenException("토큰이 비어있습니다.");
        }
    }

    private String getSubject(final String token) {
        return getParse()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    private JwtParser getParse() {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

}
