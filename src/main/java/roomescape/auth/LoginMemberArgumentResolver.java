package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.AuthenticatedMember;
import roomescape.service.member.MemberService;
import roomescape.service.member.dto.MemberResponse;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberTokenConverter memberTokenConverter;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberTokenConverter memberTokenConverter, MemberService memberService) {
        this.memberTokenConverter = memberTokenConverter;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedIn.class)
                && parameter.getParameterType().equals(AuthenticatedMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        MemberResponse member = memberTokenConverter.getMemberResponseFromCookies(request.getCookies());
        return AuthenticatedMember.from(memberService.get(member.id()));
    }
}
