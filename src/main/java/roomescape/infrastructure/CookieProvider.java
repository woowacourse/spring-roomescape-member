package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.exception.UnAuthorizedException;

@Component
public class CookieProvider {

    public Cookie create(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie invalidate(Cookie[] cookies) {
        if (cookies != null) {
            throw new UnAuthorizedException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setSecure(true);
                return cookie;
            }
        }
        throw new UnAuthorizedException(); //TODO: 개선하기
    }
}
