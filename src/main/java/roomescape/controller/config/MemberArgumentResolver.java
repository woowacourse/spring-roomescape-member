package roomescape.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.dto.request.MemberRequest;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.service.LoginService;
import roomescape.service.MemberService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final CookieManager cookieManager;
    private final MemberService memberService;
    private final LoginService loginService;

    public MemberArgumentResolver(
            final CookieManager cookieManager,
            final MemberService memberService,
            final LoginService loginService
    ) {
        this.cookieManager = cookieManager;
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberRequest.class);
    }

    // TODO 예외가 터지는 상황에 대해 고려해보기
    @Override
    public MemberRequest resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Token token = cookieManager.getToken(request.getCookies());
        Long memberId = loginService.findMemberIdByToken(token);
        Member foundMember = memberService.findMemberById(memberId);
        return new MemberRequest(foundMember);
    }
}
