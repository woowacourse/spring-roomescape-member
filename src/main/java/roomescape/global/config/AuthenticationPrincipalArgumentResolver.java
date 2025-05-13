package roomescape.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.application.service.AuthService;
import roomescape.domain.AuthMember;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthenticationAnnotation = parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
        boolean isAuthMemberType = AuthMember.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthenticationAnnotation && isAuthMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return authService.extractAuthMemberFromRequest((HttpServletRequest) webRequest.getNativeRequest());
    }
}
