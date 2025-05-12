package roomescape.global.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.MemberAuthService;
import roomescape.auth.service.dto.LoginMember;
import roomescape.global.infrastructure.AuthTokenCookieProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberAuthService service;
    private final AuthTokenCookieProvider cookieProvider;

    public LoginMemberArgumentResolver(MemberAuthService service, AuthTokenCookieProvider cookieProvider) {
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
