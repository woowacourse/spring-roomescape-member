package roomescape.auth.dto;

import java.util.Map;

public record LoginMember(Long id, String email) {

    public static LoginMember from(Map<String, String> claims) {
        Long id = Long.valueOf(claims.get("id"));
        return new LoginMember(id, claims.get("email"));
    }
}
