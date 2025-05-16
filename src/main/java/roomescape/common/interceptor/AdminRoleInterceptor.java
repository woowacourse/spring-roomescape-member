package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.jwt.JwtTokenExtractor;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.common.exception.AccessDeniedException;
import roomescape.common.exception.InvalidTokenException;
import roomescape.common.exception.MissingTokenExcpetion;
import roomescape.member.domain.Role;

@RequiredArgsConstructor
public class AdminRoleInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        final String token = jwtTokenExtractor.extractTokenFromCookies(request.getCookies());
        if (token == null) {
            throw new MissingTokenExcpetion("Token is missing");
        }

        final String email = jwtTokenProvider.getPayload(token);
        if (email == null) {
            throw new InvalidTokenException("Invalid token");
        }

        final Role role = jwtTokenProvider.getRole(token);
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }

        return true;
    }
}
