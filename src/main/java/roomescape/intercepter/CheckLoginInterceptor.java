package roomescape.intercepter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.exception.AuthFailException;

public class CheckLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookie = request.getCookies();
        if (cookie == null) {
            throw new AuthFailException("접근할 수 없는 페이지입니다.");
        }
        return true;
    }
}
