package roomescape.auth.infrastructure;

import static roomescape.auth.controller.LoginController.TOKEN_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {

    public Optional<String> extractTokenByCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

}
