package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.dto.MemberNameResponse;
import roomescape.member.service.MemberService;

public class MemberNameResponseArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN = "token";

    private final MemberService memberService;

    public MemberNameResponseArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberNameResponse.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = servletRequest.getCookies();
        String token = extractTokenFromCookie(cookies);

        return memberService.getMemberResponse(token);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), TOKEN)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
