package roomescape.auth;

import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.util.LoginTokenParser;
import roomescape.domain.Member;
import roomescape.exception.InvalidTokenException;
import roomescape.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        try {
            String token = LoginTokenParser.getLoginToken(request);
            if (token == null) {
                throw new IllegalArgumentException();
            }

            Long memberId = Long.valueOf(jwtProvider.getPayload(token));
            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isEmpty() || !member.get().role().equals("admin")) {
                throw new AuthenticationException("일반 사용자는 접근할 수 없습니다.");
            }

            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(e);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("로그인 정보가 잘못되었습니다.");
        }
    }
}
