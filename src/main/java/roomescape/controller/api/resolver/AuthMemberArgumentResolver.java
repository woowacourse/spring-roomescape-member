package roomescape.controller.api.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.exception.CustomBadRequest;
import roomescape.exception.CustomUnauthorized;
import roomescape.infrastructure.CookieProvider;
import roomescape.service.MemberService;

public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final CookieProvider cookieProvider;

    public AuthMemberArgumentResolver(final MemberService memberService, final CookieProvider cookieProvider) {
        this.memberService = memberService;
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public MemberAuthRequest resolveArgument(final MethodParameter parameter,
                                             final ModelAndViewContainer mavContainer,
                                             final NativeWebRequest webRequest,
                                             final WebDataBinderFactory binderFactory) throws Exception {
        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var authMember = cookieProvider.extractToken(request);
        try {
            memberService.findMember(authMember.id());
            return authMember;
        } catch (final CustomBadRequest e) {
            throw new CustomUnauthorized("멤버가 아닙니다.");
        }
    }
}
