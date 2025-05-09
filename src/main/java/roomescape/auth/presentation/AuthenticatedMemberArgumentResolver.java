package roomescape.auth.presentation;

import static roomescape.auth.exception.AuthErrorCode.LOGIN_REQUIRED;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.AuthorizationExtractor;
import roomescape.member.domain.Member;

@Component
public class AuthenticatedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor<Optional<String>> extractor;
    private final AuthService authService;

    public AuthenticatedMemberArgumentResolver(AuthorizationExtractor<Optional<String>> extractor,
                                               AuthService authService) {
        this.extractor = extractor;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMember.class)
                && parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = extractor.extract(request)
                .orElseThrow(() -> new AuthorizationException(LOGIN_REQUIRED));

        Member member = authService.findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName());
    }
}
