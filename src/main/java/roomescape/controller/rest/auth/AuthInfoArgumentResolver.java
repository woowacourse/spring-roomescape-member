package roomescape.controller.rest.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.auth.LoginInfo;
import roomescape.global.util.TokenManager;

@Component
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthInfo.class);
    }

    @Override
    public LoginInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                     NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = TokenManager.extractTokenFrom(request.getCookies());

        Long memberId = Long.valueOf(TokenManager.extractSubject(token));
        String memberName = TokenManager.extractClaim(token, "name");
        String memberRole = TokenManager.extractClaim(token, "role");

        return new LoginInfo(memberId, memberName, memberRole);
    }
}
