package roomescape.web.api.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.web.api.token.TokenParser;
import roomescape.web.api.token.TokenProvider;
import roomescape.web.exception.AuthorizationException;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public MemberArgumentResolver(TokenProvider tokenProvider, TokenParser tokenParser) {
        this.tokenProvider = tokenProvider;
        this.tokenParser = tokenParser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Principal resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        String token = tokenProvider.extractToken(cookies)
                .orElseThrow(AuthorizationException::new);

        return tokenParser.getPrincipal(token);
    }
}
