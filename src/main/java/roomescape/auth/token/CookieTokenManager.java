package roomescape.auth.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import roomescape.dto.TokenResponse;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieTokenManager implements TokenManager {

    private static final String TOKEN_FIELD = "token";

    @Override
    public Optional<String> extract(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_FIELD.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    @Override
    public void setToken(final HttpServletResponse response, final TokenResponse token) {
        final Cookie cookie = new Cookie(TOKEN_FIELD, token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
