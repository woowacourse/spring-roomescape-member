package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.domain.Visitor;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

public class VisitorArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public VisitorArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Visitor.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                Member member = memberService.findByToken(token);
                return new Visitor(member.getId(), member.getName(), member.getEmail(), member.getRole());
            }
        }
        return new Visitor(null, null, null, null);
    }
}