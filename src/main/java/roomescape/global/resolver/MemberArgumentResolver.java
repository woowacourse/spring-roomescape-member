package roomescape.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.jwt.TokenProvider;
import roomescape.member.application.service.MemberService;
import roomescape.global.jwt.CookieAuthorizationExtractor;
import roomescape.global.jwt.AuthorizationExtractor;
import roomescape.member.presentation.dto.MemberResponse;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor authorizationExtractor;
    private final MemberService memberService;

    public MemberArgumentResolver(MemberService memberService) {
        this.authorizationExtractor = new CookieAuthorizationExtractor();
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null || request.getCookies() == null) {
            throw new IllegalStateException("사용자 인증 정보가 없습니다.");
        }

        String token = authorizationExtractor.extract(request);
        MemberResponse memberResponse = memberService.findByToken(token);

        return new LoginMember(memberResponse.getName());
    }
}
