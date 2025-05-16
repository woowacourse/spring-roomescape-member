package roomescape.auth.controller.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.controller.annotation.LoginMemberId;
import roomescape.auth.infrastructure.JwtPayload;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.TokenExtractor;
import roomescape.global.exception.error.UnauthorizedException;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;

    public LoginMemberIdArgumentResolver(JwtTokenProvider jwtTokenProvider, TokenExtractor tokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = tokenExtractor.extractTokenByCookies(request)
                .orElseThrow(() -> new UnauthorizedException("인증 토큰이 쿠키에 존재하지 않습니다."));

        JwtPayload payload = jwtTokenProvider.getPayload(token);

        return payload.memberId();
    }

}
