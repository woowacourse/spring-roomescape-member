package roomescape.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import roomescape.business.domain.member.MemberCredential;

public final class AccessToken {

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24;
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final String value;

    private AccessToken(String value) {
        this.value = value;
    }

    public static AccessToken create(MemberCredential memberCredential) {
        return new AccessToken(buildTokenByMember(memberCredential));
    }

    public static AccessToken of(String tokenValue) {
        return new AccessToken(tokenValue);
    }

    private static String buildTokenByMember(MemberCredential memberCredential) {
        Date expiration = Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_VALIDITY_SECONDS));
        return Jwts.builder()
                .subject(memberCredential.getId().toString())
                .expiration(expiration)
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
