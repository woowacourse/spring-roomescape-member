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
import roomescape.domain.member.Member;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_KEY = "token";

    private final MemberService memberService;
    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }
        Cookie cookie = Arrays.stream(cookies)
                .filter(element -> element.getName().equals(TOKEN_KEY))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("토큰이 저장된 쿠키가 존재하지 않습니다."));

        String token = cookie.getValue();

        String payload = authService.findPayload(token);
        return memberService.findAuthInfo(payload);
    }
}
