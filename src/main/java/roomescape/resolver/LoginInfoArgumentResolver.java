package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;
import roomescape.infra.SessionLoginRepository;

@RequiredArgsConstructor
public class LoginInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionLoginRepository sessionLoginRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginRequired.class)
                && parameter.getParameterType().equals(LoginInfo.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("세션이 존재하지 않습니다.");
        }

        return sessionLoginRepository.getLoginInfo(session);
    }
}
