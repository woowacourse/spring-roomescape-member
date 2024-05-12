package roomescape.controller.rest.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.auth.LoginMember;
import roomescape.global.exception.ApplicationException;
import roomescape.global.exception.ExceptionType;
import roomescape.global.util.TokenManager;

import java.util.Arrays;

@Component
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthInfo.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = extractToken(cookies);

        Long memberId = Long.valueOf(TokenManager.extractSubject(token));
        String memberName = TokenManager.extractClaim(token, "name");
        String memberRole = TokenManager.extractClaim(token, "role");

        return new LoginMember(memberId, memberName, memberRole);
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new ApplicationException(ExceptionType.NO_COOKIE_EXIST);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ExceptionType.NO_TOKEN_EXIST))
                .getValue();
    }
}
