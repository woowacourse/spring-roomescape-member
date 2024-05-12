package roomescape.controller.resolver;

import java.util.Arrays;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;

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
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), "token"))
                .findFirst()
                .orElseThrow(AuthenticationException::new)
                .getValue();
        return authService.findByToken(token);
    }
}
