package roomescape.dto;

import io.jsonwebtoken.Claims;

public record TokenInfo(Long id, String name, String role) {

    public static TokenInfo from(final Claims claims) {
        return new TokenInfo(
                claims.get("memberId", Long.class),
                claims.get("name", String.class),
                claims.get("role", String.class)
        );
    }
}
