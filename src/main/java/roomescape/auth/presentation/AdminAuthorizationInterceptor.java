package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.CookieAuthorizationExtractor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final CookieAuthorizationExtractor extractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthorizationInterceptor(CookieAuthorizationExtractor extractor, JwtTokenProvider jwtTokenProvider) {
        this.extractor = extractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Optional<String> result = extractor.extract(request);
        if (result.isEmpty()) {
            throw new AuthorizationException(AuthErrorCode.LOGIN_REQUIRED);
        }

        String token = result.get();
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException(AuthErrorCode.INVALID_TOKEN);
        }

        Role role = jwtTokenProvider.getRole(token);
        if (role != Role.ADMIN) {
            throw new AuthorizationException(AuthErrorCode.FORBIDDEN_ACCESS);
        }
        return true;
    }
}
