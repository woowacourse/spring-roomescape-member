package roomescape.auth.domain;

import java.util.Map;
import org.springframework.http.ResponseCookie;
import roomescape.exception.MemberAuthenticationException;

public class AccessTokenCookie {
    private static final String ACCESS_TOKEN_KEY = "token";
    private static final int MIN = 60;
    private static final int ACCESS_TOKEN_MAX_AGE = 30 * MIN;

    private final String value;

    public AccessTokenCookie(String value) {
        this.value = value;
    }

    public AccessTokenCookie(Map<String, String> cookies) {
        this.value = findToken(cookies);
    }

    public String findToken(Map<String, String> cookies) {
        String value = cookies.get(ACCESS_TOKEN_KEY);
        if (value == null) {
            throw new MemberAuthenticationException();
        }
        return value;
    }

    public ResponseCookie createResponseCookie() {
        return ResponseCookie.from(ACCESS_TOKEN_KEY, value)
                .httpOnly(true)
                .path("/")
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .build();
    }

    public String getValue() {
        return value;
    }
}
