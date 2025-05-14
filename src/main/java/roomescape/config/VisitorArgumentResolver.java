package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.domain.Member;
import roomescape.member.domain.Visitor;
import roomescape.member.service.JwtUtil;
import roomescape.member.service.MemberService;

public class VisitorArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public VisitorArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
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
        if (cookies == null) {
            return new Visitor(null, null, null, null);
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                Long memberId = jwtUtil.getMemberIdFromToken(token);
                Member member = memberService.findById(memberId);
                return new Visitor(member.getId(), member.getName(), member.getEmail(), member.getRole());
            }
        }
        return new Visitor(null, null, null, null);
    }
}