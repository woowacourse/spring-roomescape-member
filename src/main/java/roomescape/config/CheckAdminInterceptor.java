package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.ForbiddenException;
import roomescape.exception.UnAuthorizedException;

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
                             @NonNull Object handler) {
        try {
            LoginMember loginMember = loginContext.getLoginMember(request);
            if (!loginMember.isAdmin()) {
                throw new ForbiddenException("관리자 권한이 없습니다.");
            }
            return true;
        } catch (Exception e) {
            throw new UnAuthorizedException("로그인이 필요합니다.");
        }
    }
}
