package roomescape.auth.util;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

public class LoginTokenParser {

    public static final String LOGIN_TOKEN_NAME = "token";

    private LoginTokenParser() {
    }

    public static String getLoginToken(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        for (var cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), LOGIN_TOKEN_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
