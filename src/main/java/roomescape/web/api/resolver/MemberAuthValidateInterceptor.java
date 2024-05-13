package roomescape.web.api.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.domain.token.TokenParser;
import roomescape.domain.token.TokenProvider;
import roomescape.web.exception.AuthenticationException;
import roomescape.web.exception.AuthorizationException;

@Component
public class MemberAuthValidateInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public MemberAuthValidateInterceptor(TokenProvider tokenProvider, TokenParser tokenParser) {
        this.tokenProvider = tokenProvider;
        this.tokenParser = tokenParser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String accessToken = tokenProvider.extractToken(cookies)
                .orElseThrow(AuthenticationException::new);

        Role role = tokenParser.getRole(accessToken);
        if (!role.isMember()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
