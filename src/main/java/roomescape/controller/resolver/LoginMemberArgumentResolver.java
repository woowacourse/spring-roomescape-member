package roomescape.controller.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.dto.LoginMemberDto;
import roomescape.infrastructure.CookieManager;
import roomescape.service.AuthService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final CookieManager cookieManager;

    public LoginMemberArgumentResolver(
            final AuthService authService,
            final CookieManager cookieManager
    ) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType() == LoginMemberDto.class;
    }

    @Override
    public LoginMemberDto resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = cookieManager.extractToken(request.getCookies());
        final Member member = authService.findMemberByToken(token);
        return new LoginMemberDto(member);
    }
}
