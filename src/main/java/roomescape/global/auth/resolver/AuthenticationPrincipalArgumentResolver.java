package roomescape.global.auth.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.auth.annotation.AdminPrincipal;
import roomescape.global.auth.annotation.MemberPrincipal;
import roomescape.global.auth.infrastructure.AuthorizationExtractor;
import roomescape.global.auth.service.AuthService;
import roomescape.member.domain.MemberRole;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthenticationPrincipalArgumentResolver(final AuthService authService,
                                                   final AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AdminPrincipal.class) ||
                parameter.hasParameterAnnotation(MemberPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        String accessToken = authorizationExtractor.extract(webRequest);
        return authService.makeUserInfo(accessToken, getMemberRole(parameter));
    }

    private MemberRole getMemberRole(final MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(AdminPrincipal.class)) {
            return MemberRole.ADMIN;
        }
        return MemberRole.USER;
    }
}
