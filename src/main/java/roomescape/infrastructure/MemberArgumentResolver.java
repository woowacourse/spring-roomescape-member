package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.infrastructure.auth.Token;
import roomescape.service.serviceimpl.LoginService;
import roomescape.service.serviceimpl.MemberService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final CookieGenerator cookieGenerator;
    private final MemberService memberService;
    private final LoginService loginService;

    public MemberArgumentResolver(
            final CookieGenerator cookieGenerator,
            final MemberService memberService,
            final LoginService loginService
    ) {
        this.cookieGenerator = cookieGenerator;
        this.memberService = memberService;
        this.loginService = loginService;
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
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Token token = cookieGenerator.getToken(request.getCookies());
        Long memberId = loginService.findMemberIdByToken(token);
        return memberService.findMemberById(memberId);
    }
}
