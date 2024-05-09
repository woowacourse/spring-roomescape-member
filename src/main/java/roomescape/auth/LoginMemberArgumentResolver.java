package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.model.LoginMember;
import roomescape.dto.MemberResponse;
import roomescape.exception.AuthorizationException;
import roomescape.model.Role;
import roomescape.service.AuthService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_FIELD = "token";

    private final AuthService authService;

    public LoginMemberArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(final MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String token = extractTokenFromRequestCookie(webRequest);
        final MemberResponse memberResponse = authService.findMemberByToken(token);
        return new LoginMember(memberResponse.id(), memberResponse.name(), Role.from(memberResponse.role()), memberResponse.email());
    }

    private String extractTokenFromRequestCookie(final NativeWebRequest webRequest) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        for (Cookie cookie : request.getCookies()) {
            if (TOKEN_FIELD.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException("토큰 정보가 존재하지 않습니다.");
    }
}
