package roomescape.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.UnAuthorizedException;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("cookie");

        if (accessToken == null) {
            throw new UnAuthorizedException("토큰이 존재하지 않습니다.");
        }

        return true;
    }
}
