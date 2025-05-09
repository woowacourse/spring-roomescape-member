package roomescape.auth.login.presentation.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.login.infrastructure.JwtTokenManager;
import roomescape.auth.login.presentation.controller.dto.annotation.LoginAdmin;
import roomescape.auth.login.presentation.controller.dto.LoginAdminInfo;

@Component
public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginAdmin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("인증할 수 없습니다."))
                .getValue();

        String role = JwtTokenManager.getRole(token);
        if (!role.equals("ADMIN")) {
            throw new IllegalStateException("인증할 수 없습니다.");
        }

        return new LoginAdminInfo(JwtTokenManager.getId(token));
    }
}
