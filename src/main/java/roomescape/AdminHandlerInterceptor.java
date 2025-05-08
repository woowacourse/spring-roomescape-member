package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
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
        // TODO: 통합 관리 해보기
        // TODO: 쿠키가 없다면 401
        if (request.getCookies() == null) {
            response.sendError(401);
            return false;
        }
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            response.sendError(401);
            return false;
        }

        // TODO: 어드민이 아니라면 403
        Member member = authService.getMemberByToken(token);
        if (member.getRole() == Role.ADMIN) {
            return true;
        }
        response.sendError(403);
        return false;
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
