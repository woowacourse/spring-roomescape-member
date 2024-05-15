package roomescape.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.request.MemberRequest;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.service.LoginService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;
    private final CookieManager cookieManager;

    public MemberArgumentResolver(LoginService loginService, CookieManager cookieManager) {
        this.loginService = loginService;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberRequest.class);
    }

    @Override
    public MemberRequest resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        try {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            Token token = cookieManager.getToken(request.getCookies());
            return new MemberRequest(loginService.check(token));
        } catch (Exception exception) {
            HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new IllegalStateException("[ERROR] 접근권한이 인증과정에서 문제가 생겼습니다.", exception);
        }
    }
}
