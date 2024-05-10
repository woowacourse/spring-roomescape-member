package roomescape.web.security;

import java.util.Arrays;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.infrastructure.authentication.AuthService;
import roomescape.infrastructure.authentication.AuthenticatedMemberProfile;
import roomescape.infrastructure.authentication.UnauthorizedException;

public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public MemberIdArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMemberId.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnauthorizedException();
        }

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(UnauthorizedException::new);

        AuthenticatedMemberProfile profile = authService.authorize(tokenCookie.getValue());

        return profile.id();
    }
}
