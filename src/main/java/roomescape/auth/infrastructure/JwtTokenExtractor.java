package roomescape.auth.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.AuthTokenExtractor;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractor implements AuthTokenExtractor<String> {

    public String extract(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> AUTH_TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseGet(null);
    }
}
