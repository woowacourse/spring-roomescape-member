package roomescape.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import roomescape.business.domain.member.Member;

public final class AccessToken {

    private static final Date ACCESS_TOKEN_EXPIRATION = Date.from(Instant.now().plusSeconds(60 * 60 * 24));
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final String value;

    private AccessToken(String value) {
        this.value = value;
    }

    public static AccessToken create(Member member) {
        return new AccessToken(buildTokenByMember(member));
    }

    public static AccessToken of(String tokenValue) {
        return new AccessToken(tokenValue);
    }

    private static String buildTokenByMember(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getRole().value())
                .expiration(ACCESS_TOKEN_EXPIRATION)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long extractMemberId() {
        return Long.valueOf(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(value)
                .getPayload()
                .getSubject());
    }

    public String getValue() {
        return value;
    }
}
