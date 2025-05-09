package roomescape.common.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(600)
                .build();
    }
}
