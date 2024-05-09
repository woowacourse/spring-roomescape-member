package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.MemberLoginResponse;
import roomescape.infrastructure.AuthorizationExtractor;
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
        final Cookie[] cookies = request.getCookies();
        final String accessToken = authorizationExtractor.extractTokenFromCookie(cookies);
        return memberService.findMemberByToken(accessToken);
    }
}
