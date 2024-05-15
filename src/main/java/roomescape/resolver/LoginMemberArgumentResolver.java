package roomescape.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.MemberRequest;
import roomescape.service.dto.MemberResponse;
import roomescape.service.dto.TokenResponse;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    public LoginMemberArgumentResolver(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberRequest.class);
    }

    @Override
    public MemberRequest resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                         NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        TokenResponse tokenResponse = authService.extractTokenByCookies(cookies);
        authService.isTokenValid(tokenResponse);
        String memberId = authService.extractMemberIdByToken(tokenResponse);
        MemberResponse memberResponse = loginMemberService.findById(Long.parseLong(memberId));
        return new MemberRequest(
                memberResponse.id(), memberResponse.name(),
                memberResponse.email(), memberResponse.password(), memberResponse.role());
    }
}
