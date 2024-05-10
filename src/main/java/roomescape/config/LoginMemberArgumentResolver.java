package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.exception.BadRequestException;
import roomescape.service.AuthService;
import roomescape.service.dto.LoginMember;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        if (request.getCookies() == null) {
            return null;
        }

        String token = extractTokenFromCookie(request.getCookies());

        Member member = authService.findLoginMember(token);
        return new LoginMember(member.getId(), member.getName());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                return cookie.getValue();
            }
        }

        throw new BadRequestException("올바르지 않은 토큰값입니다.");
    }
}
