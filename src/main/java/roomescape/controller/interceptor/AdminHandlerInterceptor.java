package roomescape.controller.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.response.LoginMember;
import roomescape.exception.AuthenticationException;
import roomescape.exception.ForbiddenException;
import roomescape.service.AuthService;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthenticationException("로그인 되지 않았습니다");
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("로그인 되지 않았습니다."));

        LoginMember loginMember = authService.getLoginMember(new LoginMemberRequest(token));

        if (loginMember.isNotAdmin()) {
            throw new ForbiddenException("관리자 전용 입니다.");
        }

        return true;
    }
}
