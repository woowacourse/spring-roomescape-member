package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.token.TokenManager;
import roomescape.exception.AuthenticationException;
import roomescape.model.LoginMember;
import roomescape.dto.MemberResponse;
import roomescape.model.Role;
import roomescape.service.AuthService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    private final TokenManager tokenManager;

    public LoginMemberArgumentResolver(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(final MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = tokenManager.extract(request)
                .orElseThrow(() -> new AuthenticationException("토큰 정보가 존재하지 않습니다."));
        final MemberResponse memberResponse = authService.findMemberByToken(token);
        return new LoginMember(memberResponse.id(), memberResponse.name(), Role.from(memberResponse.role()), memberResponse.email());
    }
}
