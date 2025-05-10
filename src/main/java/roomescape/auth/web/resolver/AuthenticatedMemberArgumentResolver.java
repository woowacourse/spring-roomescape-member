package roomescape.auth.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;
import roomescape.auth.web.cookie.TokenCookieProvider;

@RequiredArgsConstructor
@Component
public class AuthenticatedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final TokenCookieProvider tokenCookieProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = tokenCookieProvider.extractTokenFromCookie(request.getCookies());

        if (token == null || token.isBlank()) {
            throw new AuthenticationException("[ERROR] 쿠키에 토큰이 존재하지 않습니다.");
        }

        return authService.getMemberId(token);
    }
}
