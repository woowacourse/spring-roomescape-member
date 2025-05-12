package roomescape.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;
import roomescape.auth.service.CookieService;
import roomescape.entity.Member;
import roomescape.exception.impl.TokenNotFoundException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final CookieService cookieService;

    public LoginMemberArgumentResolver(
            final AuthService authService,
            final CookieService cookieService
    ) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = cookieService.extractTokenFromCookie(request.getCookies());

        if (token == null || token.isBlank()) {
            throw new TokenNotFoundException();
        }
        return authService.findMemberByToken(token);
    }
}
