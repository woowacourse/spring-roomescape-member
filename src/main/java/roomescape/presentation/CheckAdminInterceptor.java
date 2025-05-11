package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.AuthenticationInfo;
import roomescape.domain.AuthenticationTokenProvider;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthenticationTokenProvider tokenProvider;

    public CheckAdminInterceptor(final AuthenticationTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (isCurrentRequestorAdmin(request)) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }

    private boolean isCurrentRequestorAdmin(final HttpServletRequest request) {
        var optionalToken = ControllerSupports.findCookieValueByKey(request, "token");
        return optionalToken
            .filter(tokenProvider::isValidToken)
            .map(tokenProvider::extractAuthenticationInfo)
            .map(AuthenticationInfo::isAdmin)
            .orElse(false);
    }
}
