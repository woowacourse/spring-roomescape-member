package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.exception.InvalidMemberException;
import roomescape.exception.UnauthorizedException;
import roomescape.service.member.MemberService;
import roomescape.util.CookieUtils;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @Autowired
    public LoginMemberIdArgumentResolver(TokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieUtils.extractTokenFromCookie(request.getCookies());
        long memberId = tokenProvider.extractMemberId(token);
        try {
            return memberService.findById(memberId);
        } catch (InvalidMemberException e) {
            throw new UnauthorizedException("권한이 유효하지 않습니다.");
        }
    }
}
