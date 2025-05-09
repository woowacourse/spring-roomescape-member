package roomescape.common.argument;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.annotation.Auth;
import roomescape.common.exception.UnauthorizedException;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.AuthService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    @Autowired
    public MemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAuthAnnotationPresent = parameter.hasParameterAnnotation(Auth.class);
        boolean isMemberType = parameter.getParameterType().equals(Member.class);

        return isAuthAnnotationPresent && isMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null || servletRequest.getCookies() == null) {
            throw new UnauthorizedException("올바른 토큰이 아닙니다.");
        }

        Claims claims = authService.getVerifiedPayloadFrom(servletRequest.getCookies());
        return new Member(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                claims.get("email", String.class),
                Role.valueOf(claims.get("role", String.class))
        );
    }
}
