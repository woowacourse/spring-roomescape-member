package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.AuthInfo;
import roomescape.auth.service.AuthService;
import roomescape.auth.handler.RequestHandler;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;
import roomescape.member.domain.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final RequestHandler requestHandler;
    private final AuthService authService;

    public CheckAdminInterceptor(RequestHandler requestHandler, AuthService authService) {
        this.requestHandler = requestHandler;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthInfo authInfo = null;
        try {
            authInfo = authService.fetchByToken(requestHandler.extract(request));
        } catch (BusinessException | NullPointerException e) {
            throw new BusinessException(ErrorType.SECURITY_EXCEPTION);
        }
        if (authInfo == null || !authInfo.getRole().equals(Role.ADMIN)) {
            throw new BusinessException(ErrorType.NOT_ALLOWED_PERMISSION_ERROR);
        }
        return true;
    }
}
