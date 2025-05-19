package roomescape.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final AuthCookie authCookie;

    public MemberArgumentResolver(final AuthService authService, final AuthCookie authCookie) {
        this.authService = authService;
        this.authCookie = authCookie;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final String token = authCookie.getValue(request.getCookies());
        return authService.findMemberByToken(token);
    }
}
