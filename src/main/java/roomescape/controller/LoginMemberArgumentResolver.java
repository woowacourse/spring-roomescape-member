package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.api.dto.request.LoginMemberRequest;
import roomescape.exception.AuthorizationException;
import roomescape.service.MemberService;
import roomescape.service.dto.output.TokenLoginOutput;

import java.util.Arrays;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final AuthorizationException AUTHORIZATION_EXCEPTION = AuthorizationException.getInstance();
    private final MemberService memberService;

    public LoginMemberArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(LoginMemberRequest.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final String token = getToken(request);
        final TokenLoginOutput output = memberService.loginToken(token);
        return new LoginMemberRequest(output.id(),output.email(), output.password(), output.name());
    }

    private String getToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw AUTHORIZATION_EXCEPTION;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> AUTHORIZATION_EXCEPTION);
    }
}
