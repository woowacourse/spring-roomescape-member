package roomescape.web.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.auth.UnauthorizedException;

public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public MemberIdArgumentResolver(AuthService authService, CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
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
        String cookieToken = extractor.extract(request);
        if (cookieToken == null) {
            throw new UnauthorizedException();
        }

        AuthenticatedMemberProfile profile = authService.authorize(cookieToken);

        return profile.id();
    }
}
