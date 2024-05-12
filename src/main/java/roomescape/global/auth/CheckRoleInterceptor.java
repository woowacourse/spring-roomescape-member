package roomescape.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.LoginMember;
import roomescape.global.exception.AuthorizationException;

@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public CheckRoleInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginMember member = jwtTokenProvider.findMember(request);
        if (!member.isAdmin()) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }
        return true;
    }
}
