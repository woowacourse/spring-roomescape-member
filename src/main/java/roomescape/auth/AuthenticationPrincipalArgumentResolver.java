package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.custom.reason.auth.AuthNotExistsCookieException;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(
            final AuthService authService
    ) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String token = extractToken(webRequest);
        final LoginMember loginMember = authService.findLoginMemberByToken(token);
        return loginMember;
    }

    private String extractToken(final NativeWebRequest request) {
        final HttpServletRequest nativeRequest = (HttpServletRequest) request.getNativeRequest();
        final Cookie[] cookies = nativeRequest.getCookies();
        validateExistsCookies(cookies);

        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), TOKEN_NAME))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(AuthNotExistsCookieException::new);
    }

    private void validateExistsCookies(final Cookie[] cookies) {
        if(cookies == null){
            throw new AuthNotExistsCookieException();
        }
    }
}
