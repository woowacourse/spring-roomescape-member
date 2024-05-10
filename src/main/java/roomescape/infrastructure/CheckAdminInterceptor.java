package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public CheckAdminInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Role role = tokenProvider.parseAuthenticationRoleFromCookies(request.getCookies());
        if (role == Role.USER) {
            throw new CustomException(ExceptionCode.NO_AUTHENTICATION_ACCESS);
        }
        return true;
    }
}
