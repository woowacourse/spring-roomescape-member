package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import javax.naming.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.dto.LoginMemberRequest;
import roomescape.member.service.MemberService;

public class LoginMemberRequestArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberRequestArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMemberRequest.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = extractTokenFromCookie(servletRequest.getCookies());

        return memberService.getLoginMemberRequestByToken(token);
    }

    private String extractTokenFromCookie(Cookie[] cookies) throws AuthenticationException {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), "token")) {
                    return cookie.getValue();
                }
            }
        }
        throw new AuthenticationException("접근 권한 확인을 위한 쿠키가 없습니다.");
    }
}
