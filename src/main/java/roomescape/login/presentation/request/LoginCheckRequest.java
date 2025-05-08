package roomescape.login.presentation.request;

import java.util.Map;
import roomescape.global.auth.jwt.JwtHandler;

public record LoginCheckRequest(
        Long id
) {
    public static LoginCheckRequest from(final Map<String, String> claims) {
        Long id = Long.valueOf(claims.get(JwtHandler.CLAIM_ID_KEY));
        return new LoginCheckRequest(id);
    }
}
