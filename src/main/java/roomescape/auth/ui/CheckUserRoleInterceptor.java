package roomescape.auth.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.AuthTokenExtractor;
import roomescape.auth.domain.AuthTokenProvider;
import roomescape.auth.domain.RequiresRole;

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
            return true;
        }

        final String accessToken = authTokenExtractor.extract(request);
        final AuthRole role = authTokenProvider.getRole(accessToken);

        return Arrays.stream(requiresRole.authRoles())
                .anyMatch(userRole -> userRole == role);
    }
}
