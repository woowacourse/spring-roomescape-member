package roomescape.utility;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CookieUtility {

    public Optional<Cookie> findCookie(HttpServletRequest request, String key) {
        List<Cookie> cookies = List.of();
        if (request.getCookies() != null) {
            cookies = List.of(request.getCookies());
        }
        return cookies.stream()
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst();
    }

}
