package roomescape.infrastructure.auth.intercept;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.infrastructure.auth.jwt.JwtCookieResolver;
import roomescape.infrastructure.auth.jwt.JwtTokenProvider;
import roomescape.infrastructure.auth.member.UserInfo;

public class PrevRequestHandler implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final List<RequestMatcher> adminRoleRequestMatcher;

    public PrevRequestHandler(JwtTokenProvider jwtTokenProvider, List<RequestMatcher> adminRoleRequestMatcher) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminRoleRequestMatcher = adminRoleRequestMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean isMatchExist = isMatchExist(request);
        boolean isValidReqeust = true;
        if (isMatchExist) {
            String token = JwtCookieResolver.getTokenFromCookie(request);
            UserInfo userInfo = jwtTokenProvider.resolveToken(token);
            isValidReqeust = userInfo.role().equals(Role.ADMIN);
        }

        if (!isValidReqeust) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return isValidReqeust;
    }

    private boolean isMatchExist(HttpServletRequest request) {
        return adminRoleRequestMatcher.stream()
                .anyMatch(requestMatcher -> requestMatcher.isMatch(request));
    }
}
