package roomescape.global.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.config.annotation.AuthenticationPrincipal;
import roomescape.global.infrastructure.TokenCookieProvider;

@Component
public class MemberTokenMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenCookieProvider cookieProvider;

    public MemberTokenMethodArgumentResolver(TokenCookieProvider cookieProvider) {
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return cookieProvider.extractToken(request);
    }
}
