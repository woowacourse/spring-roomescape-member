package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.dto.LoginMember;
import roomescape.service.LoginService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = JwtExtractor.getTokenFromRequest(request);
        Member member = loginService.getLoginMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(),
            member.getRole());
    }
}
