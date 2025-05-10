package roomescape.member.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.application.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.AuthorizationExtractor;
import roomescape.member.infrastructure.BearerAuthorizationExtractor;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor<String> authorizationExtractor; // TODO : 이게 base, jwt, session 다양하게 적용될 수도 있는건가?
    private final AuthService authService;

    @Autowired
    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new BearerAuthorizationExtractor();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = authorizationExtractor.extract(request);
        return authService.getMember(token);
    }
}
