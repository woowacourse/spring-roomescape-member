package roomescape.auth.config;


import java.util.Arrays;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.auth.service.AuthService;
import roomescape.exception.ForbiddenException;
import roomescape.exception.UnAuthorizationException;
import roomescape.member.domain.Role;

public class AdminInterceptor implements HandlerInterceptor {
    private final AuthService adminService;

    public AdminInterceptor(final AuthService adminService) {
        this.adminService = adminService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cookie = request.getHeader(HttpHeaders.COOKIE);
        validateValueIsNotNull(cookie);
        String token = getTokenBy(cookie);
        validateValueIsNotNull(token);
        return checkAdmin(token);
    }

    private boolean checkAdmin(final String token) {
        try {
            Role role = adminService.extractMemberOf(token.substring("token=".length())).getRole();
            if (role == Role.ADMIN) {
                return true;
            }
        } catch (UnAuthorizationException e) {
            throw new UnAuthorizationException("접근 권한이 없는 사용자입니다.");
        }
        throw new ForbiddenException("인증되지 않은 사용자입니다.");
    }

    private void validateValueIsNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new UnAuthorizationException("접근 권한이 없는 사용자입니다.");
        }
    }

    private String getTokenBy(final String cookie) {
        return Arrays.stream(cookie.split(";"))
                .map(String::trim)
                .filter(it -> it.startsWith("token"))
                .findFirst()
                .orElse(null);
    }
}
