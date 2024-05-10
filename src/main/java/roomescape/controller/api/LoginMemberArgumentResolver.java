package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final AuthService authService;

    public LoginMemberArgumentResolver(final MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Member resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final var request = (HttpServletRequest) webRequest.getNativeRequest();
        final var cookies = request.getCookies();
        final var token = extractTokenFromCookie(cookies);
        final String id = authService.some(token);
        return memberService.findMemberById(id);
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (final var cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
