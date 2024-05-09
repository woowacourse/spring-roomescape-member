package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.principal.AuthenticatedMemberPrincipal;
import roomescape.auth.service.AuthService;
import roomescape.member.model.Member;
import roomescape.util.CookieUtil;

import java.util.Objects;

@Component
public class AuthenticatedMemberPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticatedMemberPrincipalArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {

        final boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Authenticated.class);
        final boolean hasAuthenticatedMemberPrincipal = AuthenticatedMemberPrincipal.class
                .isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasAuthenticatedMemberPrincipal;
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String accessToken = Objects.requireNonNull(CookieUtil.findCookie(request, "token")
                .orElseThrow(() -> new IllegalArgumentException("요청에 인증 쿠키가 존재하지 않습니다."))
                .getValue());
        final Member member = authService.findAuthenticatedMember(accessToken);

        return AuthenticatedMemberPrincipal.from(member);
    }
}
