package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.member.dto.LoginMember;
import roomescape.domain.Member;
import roomescape.service.MemberService;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {

        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        final Cookie[] cookies = request.getCookies();
        final String token = extractTokenFromCookie(cookies);
        if (cookies == null || cookies.length == 0 || token == null || token.isBlank()) {
//            throw new UnauthorizedException("잘못된 접근입니다."); // TODO 엥 이거 맞음?
            return null;
        }

        final Member member = memberService.findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole().name());
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
