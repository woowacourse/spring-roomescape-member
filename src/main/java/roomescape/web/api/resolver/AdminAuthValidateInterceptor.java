package roomescape.web.api.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.domain.token.TokenParser;
import roomescape.domain.token.TokenProvider;
import roomescape.web.exception.AuthorizationException;

@Component
public class AdminAuthValidateInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public AdminAuthValidateInterceptor(TokenProvider tokenProvider, final TokenParser tokenParser) {
        this.tokenProvider = tokenProvider;
        this.tokenParser = tokenParser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String accessToken = tokenProvider.extractToken(cookies)
                .orElseThrow(AuthorizationException::new);

        Role role = tokenParser.getRole(accessToken);
        return role.isAdmin();
    }
}
