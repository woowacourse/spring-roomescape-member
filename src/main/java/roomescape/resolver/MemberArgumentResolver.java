package roomescape.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.request.MemberRequest;
import roomescape.service.AuthService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public MemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberRequest.class);
    }

    @Override
    public MemberRequest resolveArgument(MethodParameter parameter,
                                         ModelAndViewContainer mavContainer,
                                         NativeWebRequest webRequest,
                                         WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        Long memberId = authService.findMemberId(cookies);
        return new MemberRequest(memberId);
    }
}
