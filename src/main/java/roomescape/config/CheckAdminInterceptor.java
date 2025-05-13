package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 권한을 확인하는 인터셉터입니다.
 */
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final LoginContext loginContext;

    public CheckAdminInterceptor(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        try {
            LoginMember loginMember = loginContext.getLoginMember(request);
            if (!loginMember.isAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                return false;
            }
            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }
    }
}
