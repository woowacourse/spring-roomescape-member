package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtTokenProvider;
import roomescape.exception.ForbiddenException;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Role;

import java.util.Arrays;

@Component
public class RoleCheckHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public RoleCheckHandlerInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        if (request.getCookies() == null) {
            throw new UnauthorizedException("사용자 인증 정보가 없습니다.");
        }

        String accessToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("사용자 인증 정보가 없습니다."))
                .getValue();

        Role role = Role.findByName(jwtTokenProvider.decode(accessToken, "role"));

        checkAdminRole(role);
        return true;
    }

    private void checkAdminRole(Role role) {
        if (!role.isAdminRole()) {
            throw new ForbiddenException("허용되지 않는 사용자입니다.");
        }
    }
}
