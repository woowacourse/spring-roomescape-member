package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.AuthenticatedMember;
import roomescape.service.MemberService;
import roomescape.utils.CookieParser;
import roomescape.utils.TokenManager;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTH_COOKIE_NAME = "auth_token";

    private final TokenManager tokenManager;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(TokenManager tokenManager, MemberService memberService) {
        this.tokenManager = tokenManager;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedIn.class)
                && parameter.getParameterType().equals(AuthenticatedMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieParser.searchValueFromKey(request.getCookies(), AUTH_COOKIE_NAME);
        long memberId = tokenManager.getMemberIdFromToken(token);
        return AuthenticatedMember.from(memberService.get(memberId));
    }
}
