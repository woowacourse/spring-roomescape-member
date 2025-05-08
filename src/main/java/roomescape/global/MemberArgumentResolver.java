package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Member member = (Member) session.getAttribute("LOGIN_MEMBER");
        if (member == null) {
            throw new IllegalStateException("세션에 사용자 정보가 없습니다.");
        }
        Auth auth = parameter.getParameterAnnotation(Auth.class);
        MemberRole[] allowedRoles = auth.allowedRoles();
        if (Arrays.stream(allowedRoles).noneMatch(role -> role == member.getRole())) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        return member.getId();
    }
}
