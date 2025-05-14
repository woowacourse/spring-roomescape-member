package roomescape.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginMember;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public LoginMemberArgumentResolver(final AuthService authService,
                                       final CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = extractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));
        Member member = authService.findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
