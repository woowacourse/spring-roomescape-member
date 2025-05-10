package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.auth.AuthChecker;
import roomescape.global.jwt.AuthorizationExtractor;
import roomescape.global.jwt.TokenInfo;
import roomescape.global.jwt.TokenProvider;
import roomescape.member.domain.Role;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authorizationExtractor;
    private final TokenProvider tokenProvider;

    public AuthorizationInterceptor(AuthorizationExtractor authorizationExtractor, TokenProvider tokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        final String token = authorizationExtractor.extract(request);
        final TokenInfo tokenInfo = tokenProvider.getInfo(token);

        if (!AuthChecker.checkAuthorization(handlerMethod.getMethod(), tokenInfo.getRole())) {
            log();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private static void log() {
        Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
        logger.warn("Authorization failed");
    }
}
