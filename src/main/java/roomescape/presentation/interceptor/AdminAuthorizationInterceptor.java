package roomescape.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.model.Member;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Member member = (Member) request.getAttribute("member");

        if (!member.isAdmin()) {
            return reject(response);
        }
        return true;
    }

    private boolean reject(HttpServletResponse response) {
        response.setStatus(403);
        return false;
    }
}
