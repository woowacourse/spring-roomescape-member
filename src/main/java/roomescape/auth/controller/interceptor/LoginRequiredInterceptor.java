package roomescape.auth.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.controller.annotation.LoginRequired;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.TokenExtractor;
import roomescape.global.exception.error.UnauthorizedException;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider tokenProvider;

    public LoginRequiredInterceptor(TokenExtractor tokenExtractor, JwtTokenProvider tokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        if (method.getMethodAnnotation(LoginRequired.class) == null) {
            return true;
        }

        String token = tokenExtractor.extractTokenByCookies(request)
                .orElseThrow(() -> new UnauthorizedException("인증 토큰이 쿠키에 존재하지 않습니다."));

        tokenProvider.validateToken(token);

        return true;
    }

}
