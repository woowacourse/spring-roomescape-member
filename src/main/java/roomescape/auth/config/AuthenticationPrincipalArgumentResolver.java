package roomescape.auth.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.annotation.AdminPrincipal;
import roomescape.auth.annotation.MemberPrincipal;
import roomescape.auth.exception.AuthException;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.MemberRole;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AdminPrincipal.class) ||
                parameter.hasParameterAnnotation(MemberPrincipal.class) ;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        String cookie = webRequest.getHeader("Cookie");
        if (cookie == null || !cookie.contains("token")) {
            throw new AuthException("로그인이 필요합니다.");
        }
        String accessToken = authService.extractTokenFromCookie(cookie);
        return authService.makeLoginMember(accessToken, getMemberRole(parameter));
    }

    private MemberRole getMemberRole(final MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(AdminPrincipal.class)){
            return MemberRole.ADMIN;
        }
        return MemberRole.USER;
    }
}
