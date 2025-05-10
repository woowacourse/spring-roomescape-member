package roomescape.auth.login.infrastructure.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.login.infrastructure.token.JwtTokenManager;
import roomescape.auth.login.infrastructure.token.TokenExtractor;
import roomescape.auth.login.presentation.dto.LoginMemberInfo;
import roomescape.auth.login.presentation.dto.annotation.LoginMember;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = TokenExtractor.extract(request);

        String role = JwtTokenManager.getRole(token);
        if (!role.equals("MEMBER")) {
            throw new IllegalStateException("인증할 수 없습니다.");
        }

        return new LoginMemberInfo(JwtTokenManager.getId(token));
    }
}
