package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.Role;
import roomescape.controller.member.dto.MemberInfoDto;
import roomescape.service.AuthService;

public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies).orElse("");
        MemberInfoDto memberInfoDto = authService.findByToken(token);
        if (!memberInfoDto.role().equals(Role.ADMIN)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

}
