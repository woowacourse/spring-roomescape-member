package roomescape.global.handler;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.auth.JwtProvider;
import roomescape.global.utils.Cookies;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private MemberService memberService;
    private JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = Cookies.get(request.getCookies());
        Claims claims = jwtProvider.validateToken(token);
        Long memberId = Long.parseLong(claims.getSubject());
        return memberService.getMemberFrom(memberId);
    }
}
