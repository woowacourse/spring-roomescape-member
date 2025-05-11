package roomescape.login.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.login.domain.Member;
import roomescape.login.provider.JwtTokenProvider;
import roomescape.login.service.MemberService;

@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {

        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        String token = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) {
            return null;
        }

        final String email = jwtTokenProvider.getPayload(token);
        final Member member = memberService.findMemberByEmail(email);
        return new Member(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
