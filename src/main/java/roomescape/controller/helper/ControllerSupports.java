package roomescape.controller.helper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import roomescape.exception.AuthorizationException;

public class ControllerSupports {

    public static String findCookieByKey(final HttpServletRequest request, final String key) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않거나 유효한 접근이 아닙니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> key.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("쿠키에서 인증 정보를 찾을 수 없습니다."));
    }
}
