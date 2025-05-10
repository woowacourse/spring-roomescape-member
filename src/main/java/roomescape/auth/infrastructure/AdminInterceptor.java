package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.error.ForbiddenException;
import roomescape.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        // 컨트롤러 메서드에만 적용
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        final Long memberId = Optional.ofNullable(request.getAttribute("memberId"))
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 인증 정보입니다."));

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 회원입니다."));
        if (!member.getRole().isAdmin()) {
            throw new ForbiddenException("관리자 권한이 필요합니다.");
        }
        return true;
    }
}
