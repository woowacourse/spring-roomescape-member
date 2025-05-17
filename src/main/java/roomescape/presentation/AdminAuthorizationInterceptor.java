package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.business.domain.LoginUser;
import roomescape.business.domain.Role;
import roomescape.business.service.AuthService;
import roomescape.exception.auth.UnauthorizedAccessException;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthorizationInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        try {
            final String token = CookieExtractor.extractToken(request);
            final LoginUser loginUser = authService.verifyTokenAndGetLoginUser(token);

            if (!loginUser.role().hasPermission(Role.ADMIN)) {
                throw new UnauthorizedAccessException(loginUser.role(), Role.ADMIN);
            }
            return true;
        } catch (NoSuchElementException e) {
            throw new UnauthorizedAccessException();
        }
    }
}
