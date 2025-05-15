package roomescape.global.auth.interceptor;

import static roomescape.global.auth.CookieExtractor.extractValue;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.controller.AuthController;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.service.UserService;

@Component
public class AdminRoleCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AdminRoleCheckInterceptor(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractValue(request.getCookies(), AuthController.TOKEN_KEY);
        String email = jwtTokenProvider.getPayload(token);

        User user = userService.getUserByEmail(email);
        if (user.getRoles().contains(Role.ADMIN)) {
            return true;
        }
        throw new ResourceNotFoundException();
    }
}
