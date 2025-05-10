package roomescape.infrastructure.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infrastructure.jwt.JwtCookieResolver;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.infrastructure.member.MemberInfo;

public class PreHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final List<RequestMatcher> requestMatcher;

    public PreHandlerInterceptor(JwtTokenProvider jwtTokenProvider, List<RequestMatcher> requestMatcher) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.requestMatcher = requestMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isMatch(request)) {
            return true;
        }
        String token = JwtCookieResolver.getTokenFromCookie(request);
        MemberInfo memberInfo = jwtTokenProvider.resolveToken(token);
        if (memberInfo == null || !memberInfo.isAdmin()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return true;
    }

    private boolean isMatch(HttpServletRequest request) {
        return !requestMatcher.stream()
                .allMatch(matcher -> matcher.isMatch(request));
    }
}
