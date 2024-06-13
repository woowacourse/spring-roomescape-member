package roomescape.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.MemberRole;
import roomescape.global.exception.UnAuthorizedException;
import roomescape.infrastructure.JwtTokenProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("cookie");
        if (accessToken == null) {
            throw new UnAuthorizedException("토큰이 존재하지 않습니다.");
        }

        String role = jwtTokenProvider.getPayload("token", accessToken, "role");

        if (!role.equals(MemberRole.ADMIN.name())) {
            throw new UnAuthorizedException("접근 권한이 없습니다.");
        }

        return true;
    }
}
