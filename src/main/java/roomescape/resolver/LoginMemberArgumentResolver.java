package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.service.LoginMemberService;
import roomescape.util.CookieTokenExtractor;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginMemberService loginMemberService;
    private final CookieTokenExtractor authorizationExtractor;

    public LoginMemberArgumentResolver(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
        this.authorizationExtractor = new CookieTokenExtractor();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = authorizationExtractor.extract(request);
        return loginMemberService.findMemberByToken(token);
    }
}
