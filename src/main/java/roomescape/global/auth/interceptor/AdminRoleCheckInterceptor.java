package roomescape.global.auth.interceptor;

import static roomescape.global.auth.CookieExtractor.extractValue;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.controller.AuthController;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.repository.UserDao;

@Component
public class AdminRoleCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;

    public AdminRoleCheckInterceptor(JwtTokenProvider jwtTokenProvider, UserDao userDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractValue(request.getCookies(), AuthController.TOKEN_KEY);
        String email = jwtTokenProvider.getPayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("사용자가 존재하지 않습니다."));
        if (user.getRoles().contains(Role.ADMIN)) {
            return true;
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return false;
    }
}
