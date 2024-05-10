package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.MemberLoginResponse;
import roomescape.service.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginMemberArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new AuthorizationExtractor();
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberLoginResponse.class);
    }

    @Override
    public MemberLoginResponse resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String accessToken = authorizationExtractor.extractToken(request);
        return memberService.findMemberByToken(accessToken);
    }
}
