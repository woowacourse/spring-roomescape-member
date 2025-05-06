package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.CookieAuthorizationExtractor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final CookieAuthorizationExtractor extractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthorizationInterceptor(CookieAuthorizationExtractor extractor, JwtTokenProvider jwtTokenProvider) {
        this.extractor = extractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String token = extractor.extract(request);

        if (token == null) {
            return handleRedirectWithAlert(response, "로그인이 필요합니다.", "/login");
        }

        if (!jwtTokenProvider.validateToken(token)) {
            return handleRedirectWithAlert(response, "유효하지 않은 토큰입니다.", "/login");
        }

        Role role = jwtTokenProvider.getRole(token);
        if (role != Role.ADMIN) {
            response.sendRedirect("/error/403.html");
            return false;
        }

        return true;
    }

    private boolean handleRedirectWithAlert(HttpServletResponse response, String message, String redirectUrl) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
                "<script>alert('" + message + "'); location.href='" + redirectUrl + "';</script>"
        );
        return false;
    }
}
