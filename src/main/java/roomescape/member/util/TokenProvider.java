package roomescape.member.util;

import io.jsonwebtoken.Jwts;

public interface TokenProvider {
    String createToken(String payload);
    public String getPayload(String token);
}
