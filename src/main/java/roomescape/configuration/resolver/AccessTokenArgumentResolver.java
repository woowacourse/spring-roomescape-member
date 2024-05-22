package roomescape.configuration.resolver;

import java.util.Objects;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;
import roomescape.util.CookieUtil;

@Component
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AccessTokenArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccessToken.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        Cookie[] cookies = CookieUtil.requireNonnull(request.getCookies(), AuthenticationException::new);
        return authService.findFromCookies(cookies);
    }
}
