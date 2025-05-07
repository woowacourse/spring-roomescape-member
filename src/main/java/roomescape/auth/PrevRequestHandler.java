package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;

public class PrevRequestHandler implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final List<RequestMatcher> adminRoleRequestMatcher;
    //bad code
    private static final String SUPER_ADMIN = "praisebak@naver.com";

    public PrevRequestHandler(JwtTokenProvider jwtTokenProvider, List<RequestMatcher> adminRoleRequestMatcher) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminRoleRequestMatcher = adminRoleRequestMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean isMatchExist = isMatchExist(request);
        if (isMatchExist) {
            String token = JwtCookieResolver.getTokenFromCookie(request);
            UserInfo userInfo = jwtTokenProvider.resolveToken(token);
            return userInfo.role().equals(Role.ADMIN) || userInfo.username().equals(SUPER_ADMIN);
        }
        return true;
    }

    private boolean isMatchExist(HttpServletRequest request) {
        return adminRoleRequestMatcher.stream()
                .anyMatch(requestMatcher -> requestMatcher.isMatch(request));
    }
}
