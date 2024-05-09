package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.LoginMemberResponse;
import roomescape.service.dto.TokenResponse;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    @Autowired
    public LoginMemberArgumentResolver(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                       NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        TokenResponse tokenResponse = authService.extractTokenByCookies(request);
        String email = authService.extractEmailByToken(tokenResponse); //emailDto 도 있어야 하나
        LoginMemberResponse loginMemberResponse = loginMemberService.findByEmail(email);
        return new LoginMember(
                loginMemberResponse.id(), loginMemberResponse.name(),
                loginMemberResponse.email(), loginMemberResponse.password());
    }
}
