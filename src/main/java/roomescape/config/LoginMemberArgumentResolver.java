package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.entity.LoginMember;
import roomescape.auth.service.MemberAuthService;
import roomescape.infrastructure.TokenCookieProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberAuthService service;
    private final TokenCookieProvider cookieProvider;

    public LoginMemberArgumentResolver(MemberAuthService service, TokenCookieProvider cookieProvider) {
        this.service = service;
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = cookieProvider.extractToken(request);
        return service.getLoginMemberByToken(token);
    }
}
