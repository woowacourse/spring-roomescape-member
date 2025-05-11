package roomescape.infrastructure.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exceptions.auth.AuthenticationException;
import roomescape.exceptions.auth.AuthorizationException;
import roomescape.infrastructure.jwt.JwtCookieResolver;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.infrastructure.member.MemberInfo;

public class AuthPreHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthPreHandlerInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String token = JwtCookieResolver.getTokenFromCookie(request);
            MemberInfo memberInfo = jwtTokenProvider.resolveToken(token);
            if (memberInfo == null || !memberInfo.isAdmin()) {
                throw new AuthorizationException("권한이 없는 사용자입니다.");
            }
            return true;
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } catch (AuthorizationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }
    }
}
