package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.auth.AuthenticationException;

public class AuthenticationPrincipalResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(AuthenticationPrincipal.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request != null && request.getCookies() != null) {
            String token = getTokenFromCookie(request);
            return jwtTokenProvider.resolveToken(token);
        }
        if (request != null) {
            resetToken(request);
        }

        throw new AuthenticationException("UserInfo를 파싱하는데 실패하였습니다");
    }

    private void resetToken(HttpServletRequest request) {
        String jwtCookieKey = JwtTokenProvider.getCookieKey();
        Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
                .filter((cookie) -> jwtCookieKey.equals(cookie.getName()))
                .findAny();
        tokenCookie.ifPresent(cookie -> cookie.setMaxAge(0));
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        String jwtCookieKey = JwtTokenProvider.getCookieKey();
        return Arrays.stream(request.getCookies())
                .filter((cookie) -> jwtCookieKey.equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("유효하지 않은 토큰입니다"));
    }
}
