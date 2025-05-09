package roomescape.presentation.methodresolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.exception.AuthException;
import roomescape.service.JwtProvider;
import roomescape.service.JwtProvider.JwtPayload;

@Component
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    public AuthInfoArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie cookie = getTokenCookie(request);
        JwtPayload jwtPayload = jwtProvider.extractPayload(cookie.getValue());
        return new AuthInfo(jwtPayload.name());
    }

    private static Cookie getTokenCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(each -> each.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new AuthException("인증 쿠키값이 존재하지 않습니다."));
    }
}
