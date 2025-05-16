package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.entity.Role;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;
import roomescape.infra.SessionLoginRepository;

@RequiredArgsConstructor
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final SessionLoginRepository sessionLoginRepository;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            processUnauthorized(request, response);
            return false;
        }

        try {
            final LoginInfo loginInfo = sessionLoginRepository.getLoginInfo(session);
            if (Role.ADMIN.equals(loginInfo.role())) {
                return true;
            }

            processUnauthorized(request, response);
            return false;
        } catch (AccessDeniedException e) {
            processUnauthorized(request, response);
            return false;
        }
    }

    private void processUnauthorized(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String accept = request.getHeader(HttpHeaders.ACCEPT);
        final boolean isApiRequest = accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE);

        if (isApiRequest) {
            throw new AccessDeniedException("관리자 권한이 없습니다.");
        }
        response.sendRedirect("/login");
    }
}
