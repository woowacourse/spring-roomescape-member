package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.AuthenticationService;
import roomescape.domain.User;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    public CheckAdminInterceptor(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isCurrentRequestorAdmin(request)) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }

    private Boolean isCurrentRequestorAdmin(final HttpServletRequest request) {
        var optionalToken = ControllerSupports.findCookieValueByKey(request, "token");
        return optionalToken
            .filter(authenticationService::isAvailableToken)
            .map(authenticationService::getUserByToken)
            .map(User::isAdmin)
            .orElse(false);
    }

}
