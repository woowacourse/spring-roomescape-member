package roomescape.domain.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.service.AuthService;

@Component
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public TokenArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidAuthorizationException("쿠키가 존재하지 않습니다!");
        }

        return authService.getLoginUser(cookies);
    }
}
