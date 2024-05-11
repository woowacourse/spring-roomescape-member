package roomescape.auth.controller;


import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.auth.service.AuthService;
import roomescape.exception.AuthorizationException;
import roomescape.member.domain.Role;

public class AdminInterceptor implements HandlerInterceptor {
    private final AuthService adminService;

    public AdminInterceptor(final AuthService adminService) {
        this.adminService = adminService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cookie = request.getHeader(HttpHeaders.COOKIE);
        if (checkNull(response, cookie)) {
            return false;
        }
        String token = getTokenBy(cookie);
        if (checkNull(response, token)) {
            return false;
        }
        return checkAdmin(response, token);
    }

    private boolean checkAdmin(final HttpServletResponse response, final String token) throws IOException {
        try {
            Role role = adminService.loginCheck(token.substring("token=".length())).getRole();
            if (role == Role.ADMIN) {
                return true;
            }
        } catch (AuthorizationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }

    private boolean checkNull(final HttpServletResponse response, final String value) throws IOException {
        if (value == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }
        return false;
    }

    private String getTokenBy(final String cookie) {
        return Arrays.stream(cookie.split(";"))
                .map(String::trim)
                .filter(it -> it.startsWith("token"))
                .findFirst()
                .orElse(null);
    }
}
