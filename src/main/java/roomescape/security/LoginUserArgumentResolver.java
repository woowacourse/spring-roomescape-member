package roomescape.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN = "token";

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public MemberResponse resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String token = extractTokenFromCookie(webRequest);
        Long id = jwtTokenProvider.getMemberId(token);

        return memberService.getById(id);
    }

    private String extractTokenFromCookie(NativeWebRequest webRequest) {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (nativeRequest == null) {
            return null;
        }

        Cookie[] cookies = nativeRequest.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
