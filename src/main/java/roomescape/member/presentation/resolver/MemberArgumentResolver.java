package roomescape.member.presentation.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.jwt.AuthorizationExtractor;
import roomescape.global.jwt.CookieAuthorizationExtractor;
import roomescape.global.jwt.TokenProvider;
import roomescape.member.application.service.MemberService;
import roomescape.member.domain.Member;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor authorizationExtractor;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public MemberArgumentResolver(TokenProvider tokenProvider,
                                  CookieAuthorizationExtractor cookieAuthorizationExtractor,
                                  MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.authorizationExtractor = cookieAuthorizationExtractor;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = authorizationExtractor.extract(request);
        Long id = tokenProvider.getInfo(token).getId();
        return memberService.findById(id);
    }
}
