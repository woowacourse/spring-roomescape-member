package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.exception.UnauthorizedException;
import roomescape.infrastructure.JwtTokenProvider;

@Slf4j
public class AdminAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor authorizationExtractor;

    public AdminAuthenticationInterceptor(final JwtTokenProvider jwtTokenProvider,
                                          final AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {

        String token = authorizationExtractor.extract(request)
                .orElseThrow(UnauthorizedException::new);

        if (token.isEmpty()) {
            response.sendRedirect("/");
            return false;
        }
        String role = jwtTokenProvider.getTokenInfo(token).role();

        log.info("Role: {}", role);
        if (!"ADMIN".equals(role)) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
