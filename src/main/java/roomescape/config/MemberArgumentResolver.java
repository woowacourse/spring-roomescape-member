package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.infrastructure.TokenExtractor;
import roomescape.service.member.MemberService;
import roomescape.service.security.AuthService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final MemberService memberService;

    public MemberArgumentResolver(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        String token = TokenExtractor.extract((HttpServletRequest) webRequest.getNativeRequest());
        Member member = authService.findMemberByToken(token);

        return memberService.findMember(member.getId());
    }
}
