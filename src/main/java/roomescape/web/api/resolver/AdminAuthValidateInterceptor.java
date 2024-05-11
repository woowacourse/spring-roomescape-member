package roomescape.web.api.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.TokenProvider;
import roomescape.web.exception.AuthorizationException;

@Component
public class AdminAuthValidateInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;

    public AdminAuthValidateInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String accessToken = tokenProvider.extractToken(cookies)
                .orElseThrow(AuthorizationException::new);

        String role = tokenProvider.getRole(accessToken);
        return role.equals("ADMIN");
    }
}
