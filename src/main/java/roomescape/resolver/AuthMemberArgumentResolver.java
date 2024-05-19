package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.exception.CustomException;
import roomescape.exception.CustomUnauthorized;
import roomescape.util.CookieProvider;

public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieProvider cookieProvider;

    public AuthMemberArgumentResolver(final CookieProvider cookieProvider) {
        this.cookieProvider = cookieProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public MemberAuthRequest resolveArgument(final MethodParameter parameter,
                                             final ModelAndViewContainer mavContainer,
                                             final NativeWebRequest webRequest,
                                             final WebDataBinderFactory binderFactory) throws Exception {
        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        try {
            return cookieProvider.extractToken(request);
        } catch (final CustomException e) {
            throw new CustomUnauthorized("멤버가 아닙니다.");
        }
    }
}
