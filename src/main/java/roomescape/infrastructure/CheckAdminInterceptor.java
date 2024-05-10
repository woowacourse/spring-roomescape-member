package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public CheckAdminInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = tokenProvider.extractTokenFromCookie(request.getCookies());
        Role role = tokenProvider.parseAuthenticationRole(token);
        return role == Role.ADMIN;
    }
}
