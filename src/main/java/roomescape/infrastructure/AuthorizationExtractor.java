package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.exception.AuthorizationException;

@Component
public class AuthorizationExtractor {

    public String extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("쿠키에 토큰 정보가 비어있습니다."))
                .getValue();
    }
}
