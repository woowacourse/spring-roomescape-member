package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.role.MemberRole;

public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenManager tokenManager;

    public LoginMemberIdArgumentResolver(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = AuthInformationExtractor.extractToken(request);
        MemberRole memberRole = tokenManager.extract(token);
        return memberRole.getMemberId();
    }
}
