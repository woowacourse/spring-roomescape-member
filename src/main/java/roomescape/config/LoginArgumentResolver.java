package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotaions.Login;
import roomescape.auth.JwtTokenProvider;
import roomescape.member.controller.MemberLoginApiController;
import roomescape.member.dto.LoginMember;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginAnnotation = parameter.getParameterAnnotation(Login.class) != null;
        boolean isLoginMemberClass = LoginMember.class.equals(parameter.getParameterType());
        return isLoginAnnotation && isLoginMemberClass;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        String token = extractTokenFromCookie(cookies);
        LoginMember loginMember = jwtTokenProvider.getMember(token);
        if (loginMember == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return loginMember;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(MemberLoginApiController.COOKIE_TOKEN_KEY))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("인증되지 않은 사용자 입니다."));
    }
}
