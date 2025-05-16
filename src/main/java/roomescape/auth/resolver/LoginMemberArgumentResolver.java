package roomescape.auth.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;
import roomescape.auth.util.CookieHandler;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        final HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        final Cookie[] cookies = nativeRequest.getCookies();

        final String token = CookieHandler.extractTokenFromCookies(cookies);
        return authService.getMemberByToken(token);
    }
}
