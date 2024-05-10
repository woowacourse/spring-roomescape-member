package roomescape.controller.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.model.LoginMember;
import roomescape.service.AuthService;
import roomescape.service.dto.MemberInfo;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // TODO: 쿠키가 없는 경우?
        String cookie = webRequest.getHeader("cookie");
        String token = cookie.substring(6);
        MemberInfo loginMember = authService.checkToken(token);
        return new LoginMember(loginMember.getId(), loginMember.getName(), loginMember.getEmail());
    } // TODO: check
}
