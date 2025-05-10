package roomescape.global.util;

import jakarta.servlet.http.Cookie;
import java.util.Objects;

public class CookieUtils {

    public static Cookie createBasic(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie extractFromCookiesByName(Cookie[] cookies, String name) {
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), name)) {
                return cookie;
            }
        }

        throw new IllegalStateException(name + "의 이름을 가진 쿠키를 찾을 수 없습니다.");
    }
}
