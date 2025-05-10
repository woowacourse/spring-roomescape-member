package roomescape.config;

import static roomescape.config.AuthenticationInterceptor.LOGIN_MEMBER_ATTRIBUTE_NAME;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 권한을 확인하는 인터셉터입니다.
 */
public class CheckAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        LoginMember loginMember = extractLoginMember(request);
        if (loginMember == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }
        if (!loginMember.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
            return false;
        }
        return true;
    }

    private LoginMember extractLoginMember(HttpServletRequest request) {
        return (LoginMember) request.getAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME);
    }
}
