package roomescape.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class AuthenticationToken {

    private final SecretKey key;
    private final String value;

    public AuthenticationToken(final SecretKey key, final String value) {
        this.key = key;
        this.value = value;
    }

    public Claims getClaims() {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(value)
                .getPayload();
    }

    public String getValue() {
        return value;
    }
}
