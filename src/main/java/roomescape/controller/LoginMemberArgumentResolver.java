package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.exception.AuthorizationException;
import roomescape.domain.Member;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.service.JwtTokenProvider;
import roomescape.service.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider, AuthorizationExtractor extractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMemberRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = extractor.extractTokenFromCookie(request);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("인증 정보가 올바르지 않습니다.");
        };

        String payload = jwtTokenProvider.getPayload(token);
        Long memberId = Long.parseLong(payload);
        Member member = memberService.findMemberById(memberId);
        return new LoginMemberRequest(member.getId(), member.getName());
    }
}
