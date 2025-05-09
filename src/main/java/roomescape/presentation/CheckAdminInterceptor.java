package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.AuthenticationService;
import roomescape.application.AuthorizationException;
import roomescape.domain.User;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    public CheckAdminInterceptor(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var user = findUser(request);
        if (user.isAdmin()) {
            return true;
        }
        throw new AuthorizationException("관리자만 접근 가능합니다.");
    }

    private User findUser(final HttpServletRequest request) {
        var optionalToken = ControllerSupports.findCookieValueByKey(request, "token");
        var token = optionalToken.orElseThrow(() -> new AuthorizationException("사용자 인증이 필요합니다."));
        return authenticationService.findUserByToken(token);
    }
}
