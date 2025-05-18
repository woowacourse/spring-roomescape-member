package roomescape.infrastructure;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.business.LoginInformation;
import roomescape.business.Member;
import roomescape.business.service.AuthenticationService;
import roomescape.exception.AuthException;

@Named
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationService authenticationService;

    @Inject
    public LoginArgumentResolver(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginInformation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException("토큰이 존재하지 않습니다.");
        }
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new AuthException("토큰이 존재하지 않습니다."))
                .getValue();
        Member member = authenticationService.findMemberByToken(token);
        return new LoginInformation(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
