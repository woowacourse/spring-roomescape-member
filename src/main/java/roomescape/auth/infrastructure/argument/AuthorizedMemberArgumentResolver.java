package roomescape.auth.infrastructure.argument;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.infrastructure.AuthorizationContext;
import roomescape.auth.infrastructure.Role;
import roomescape.common.globalexception.ForbiddenException;
import roomescape.common.globalexception.InternalServerException;
import roomescape.common.globalexception.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class AuthorizedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public AuthorizedMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthorizedMember.class);
    }

    @Override
    public Member resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new InternalServerException("NativeWebRequest 를 HttpServletRequest 로 변환하는 데에 실패했습니다.");
        }
        AuthorizationContext authorizationContext = (AuthorizationContext) request.getAttribute(
            AuthorizationContext.ATTRIBUTE_NAME);
        validatePrincipal(authorizationContext);
        return memberService.findByEmail(authorizationContext.identifier())
            .orElseThrow(() -> new UnauthorizedException("잘못된 인증 정보입니다."));
    }

    private void validatePrincipal(AuthorizationContext authorizationContext) {
        if (authorizationContext == null) {
            throw new UnauthorizedException("인증 정보가 없습니다.");
        }
        if (!authorizationContext.role().equals(Role.MEMBER)) {
            throw new ForbiddenException("사용자 권한이 없습니다.");
        }
    }
}
