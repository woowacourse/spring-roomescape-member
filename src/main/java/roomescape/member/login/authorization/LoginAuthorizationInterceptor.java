package roomescape.member.login.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.AuthorizationException;

@Component
public class LoginAuthorizationInterceptor implements HandlerInterceptor {
    private static final String ADMIN_ROLE_NAME = "admin";
    private static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "토큰이 존재하지 않습니다";

    private final TokenAuthorizationHandler tokenAuthorizationHandler;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginAuthorizationInterceptor(
            TokenAuthorizationHandler tokenAuthorizationHandler,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.tokenAuthorizationHandler = tokenAuthorizationHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler
    ) {
        String token = tokenAuthorizationHandler.extractToken(httpServletRequest);
        if (token == null || token.isBlank()) {
            throw new AuthorizationException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }
        String role = jwtTokenProvider.getPayloadRole(token);
        return role.equals(ADMIN_ROLE_NAME);
    }
}
