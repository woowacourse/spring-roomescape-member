package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.service.AuthService;

public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public MemberIdArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class);
    }

    @Override
    public Long resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var cookies = request.getCookies();
        final var token = extractTokenFromCookie(cookies);
        return Long.parseLong(authService.some(token));
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (final var cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
