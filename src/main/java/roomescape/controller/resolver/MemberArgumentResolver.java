package roomescape.controller.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final AuthenticationService authService;

    public MemberArgumentResolver(MemberService memberService, AuthenticationService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Cookie[] cookies = Objects.requireNonNull(request.getCookies(), "쿠키가 비어있습니다.");
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
        Long id = authService.findByToken(token);
        Member member = memberService.findById(id);

        return member;
    }
}
