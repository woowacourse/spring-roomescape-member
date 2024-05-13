package roomescape.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class CheckLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("cookie");

        if (accessToken == null) {
            throw new AuthorizationException("토큰이 존재하지 않습니다.");
        }

        return true;
    }
}
