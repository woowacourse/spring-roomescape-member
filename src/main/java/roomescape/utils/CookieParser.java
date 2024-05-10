package roomescape.utils;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import roomescape.exception.EmptyParameterException;

public class CookieParser {
    private CookieParser() {
    }

    public static String searchValueFromKey(Cookie[] cookies, String key) {
        if (cookies == null) {
            throw new EmptyParameterException("쿠키가 존재하지 않습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow();
    }
}
