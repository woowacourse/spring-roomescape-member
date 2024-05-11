package roomescape.infrastructure.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.AuthService;
import roomescape.application.dto.MemberResponse;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckLoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractTokenFrom(request);
        MemberResponse memberResponse = authService.findMemberByToken(token);
        if (memberResponse.role().isAdmin()) {
            return true;
        }
        throw new RoomescapeException(RoomescapeErrorCode.FORBIDDEN);
    }

    private String extractTokenFrom(HttpServletRequest request) {
        return Arrays.stream(getCookiesFrom(request))
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .map(Cookie::getValue)
                .orElse("");
    }

    private Cookie[] getCookiesFrom(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RoomescapeException(RoomescapeErrorCode.UNAUTHORIZED);
        }
        return cookies;
    }
}
