package roomescape.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
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
        String token = extractValue(request.getCookies(), "token");
        String email = jwtTokenProvider.getPayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("사용자가 존재하지 않습니다."));
        if (user.getRoles().contains(Role.ADMIN)) {
            return true;
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return false;
    }

    private String extractValue(Cookie[] cookies, String key) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException("쿠키를 추출하는데 실패했습니다.");
    }
}
