package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.service.MemberService;
import roomescape.service.helper.CookieExtractor;
import roomescape.service.helper.JwtTokenProvider;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final CookieExtractor cookieExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(CookieExtractor cookieExtractor,
                                       JwtTokenProvider jwtTokenProvider,
                                       MemberService memberService) {
        this.cookieExtractor = cookieExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = cookieExtractor.getToken(request.getCookies());
        String email = jwtTokenProvider.getMemberEmail(token);
        return memberService.findByEmail(email); // TODO: 조회 로직의 필요 여부 판단해보기(지연 로딩 안되지 않나?)
    }
}
