package roomescape.auth.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.auth.infrastructure.TokenService;
import roomescape.auth.web.support.CookieAuthorizationExtractor;
import roomescape.domain.member.model.Role;

@Component
@RequiredArgsConstructor
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieAuthorizationExtractor.extract(request);
        AuthenticatedMember authenticatedMember = tokenService.resolveAuthenticatedMember(token);
        if (Objects.equals(authenticatedMember.role(), Role.ADMIN)) {
            return true;
        }

        // TODO : HandlerExceptionResolver 커스텀 클래스 구현해 처리하기
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\": \"관리자 권한이 필요합니다.\"}");
        return false;
    }
}
