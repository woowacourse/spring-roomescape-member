package roomescape.auth.infrastructure.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JwtHandler {

    private static final String TOKEN_NAME = "token";

    public void setJwt(HttpServletResponse httpServletResponse, String jwt) {
        Cookie cookie = new Cookie(TOKEN_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);

        httpServletResponse.addCookie(cookie);
    }

    public Optional<String> parseJwt(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
            .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst();
    }

    public void removeJwt(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        httpServletResponse.addCookie(cookie);
    }
}
