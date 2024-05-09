package roomescape.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import roomescape.member.model.MemberEmail;
import roomescape.member.model.MemberRole;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class TokenProvider {

    private final SecretKey tokenSecretKey;
    private final long tokenExpirationPeriod;

    public TokenProvider(final SecretKey tokenSecretKey, final long tokenExpirationPeriod) {
        this.tokenSecretKey = tokenSecretKey;
        this.tokenExpirationPeriod = tokenExpirationPeriod;
    }

    public AuthenticationToken createToken(final MemberEmail email, final MemberRole role) {
        return new AuthenticationToken(tokenSecretKey, generateTokenValue(email, role));
    }

    private String generateTokenValue(final MemberEmail email, final MemberRole role) {
        final Claims claim = createClaims(email, role);
        return Jwts.builder()
                .claims(claim)
                .signWith(tokenSecretKey)
                .compact();
    }

    private Claims createClaims(final MemberEmail email, final MemberRole role) {
        return Jwts.claims()
                .subject(email.value())
                .issuedAt(new Date(currentTimeMillis()))
                .expiration(createExpirationDate())
                .add("role", role.name())
                .build();
    }

    private Date createExpirationDate() {
        return new Date(new Date().getTime() + tokenExpirationPeriod);
    }

    public AuthenticationToken convertAuthenticationToken(final String accessToken) {
        return new AuthenticationToken(tokenSecretKey, accessToken);
    }
}
