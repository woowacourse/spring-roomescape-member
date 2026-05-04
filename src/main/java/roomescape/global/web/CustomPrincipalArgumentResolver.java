package roomescape.global.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.auth.Accessor;
import roomescape.global.auth.CustomPrincipal;
import roomescape.global.auth.ForbiddenException;

public class CustomPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ROLE_HEADER = "role";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CustomPrincipal.class)
                && parameter.getParameterType().equals(Accessor.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String role = request.getHeader(ROLE_HEADER);
        if (role == null) {
            throw new ForbiddenException("role header가 필요합니다.");
        }

        return new Accessor(role);
    }
}
