package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.domain.Role;
import roomescape.exception.ForbiddenException;
import roomescape.exception.UnauthorizedException;
import roomescape.service.AuthService;

@Component
public class CheckRoleHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckRoleHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);
        if (checkRole == null) {
            return true;
        }

        String token = JwtExtractor.extractFromRequest(request);
        LoginMember loginMember = authService.getLoginMemberByToken(token);
        if (loginMember == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }
        Role[] roles = checkRole.value();
        if (Arrays.stream(roles).noneMatch(role -> role == loginMember.getRole())) {
            throw new ForbiddenException("접근 불가한 페이지입니다.");
        }
        return true;
    }
}
