package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

    public String extractByCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return ""; //토큰이 없으면 빈 문자열을 리턴해도 괜찮은가?
    }

}
