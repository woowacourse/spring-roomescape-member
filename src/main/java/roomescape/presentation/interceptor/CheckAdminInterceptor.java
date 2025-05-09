package roomescape.presentation.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.TokenLoginService;
import roomescape.domain.model.Member;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenLoginService tokenLoginService;

    public CheckAdminInterceptor(final TokenLoginService tokenLoginService) {
        this.tokenLoginService = tokenLoginService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String token = extractTokenFromCookie(request.getCookies());
        Member member = tokenLoginService.check(token);
        request.setAttribute("member", member);
        if (member == null || !member.isAdmin()) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
