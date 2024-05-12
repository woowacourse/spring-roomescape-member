package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AdminLoginInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (jwtTokenProvider.doesNotRequestHasCookie(request) || jwtTokenProvider.doesNotRequestHasToken(request)) {
            response.setStatus(401);
            return false;
        }

        Role role = jwtTokenProvider.extractRole(request);
        if (Role.ADMIN != role) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
