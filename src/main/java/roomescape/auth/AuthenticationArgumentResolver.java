package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.InvalidTokenException;
import roomescape.infrastructure.JwtTokenProvider;

public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider tokenProvider;

    public AuthenticationArgumentResolver(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new InvalidTokenException();
        }
        String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(InvalidTokenException::new)
                .getValue();

        String subject = tokenProvider.extractSubject(token);
        return Long.parseLong(subject);
    }
}
