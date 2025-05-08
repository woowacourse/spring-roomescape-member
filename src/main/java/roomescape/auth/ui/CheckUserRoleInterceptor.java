package roomescape.auth.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.AuthTokenExtractor;
import roomescape.auth.domain.AuthTokenProvider;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.UserRole;

@RequiredArgsConstructor
public class CheckUserRoleInterceptor implements HandlerInterceptor {

    private final AuthTokenExtractor<String> authTokenExtractor;
    private final AuthTokenProvider authTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        final RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);

        if (requiresRole == null) {
            return false;
        }

        final String accessToken = authTokenExtractor.extract(request);
        final UserRole role = authTokenProvider.getRole(accessToken);

        return Arrays.stream(requiresRole.userRoles())
                .anyMatch(userRole -> userRole == role);
    }
}
