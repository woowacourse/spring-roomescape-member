package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.dto.LoginMember;
import roomescape.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {

        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final Long memberId = (Long) request.getAttribute("memberId");
        if (memberId == null) {
            throw new UnauthorizedException("인증되지 않은 요청입니다.");
        }

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("인증 정보가 유효하지 않습니다. id: " + memberId));
        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole());
    }
}
