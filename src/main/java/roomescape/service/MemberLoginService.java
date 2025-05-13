package roomescape.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.exception.InvalidAuthException;

@Service
public class MemberLoginService {

    private final AuthService authService;
    private final MemberService memberService;

    public MemberLoginService(AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public MemberResponse findByToken(final String token) {
        final String email = authService.getPayload(token);
        final Optional<Member> member = memberService.findByEmail(email);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return MemberResponse.from(member.get());
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final String accessToken = authService.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    public String extractToken(final HttpServletRequest request) {
        return authService.extractToken(request);
    }

    public void validateToken(final String accessToken) {
        if (authService.validateToken(accessToken)) {
            return;
        }
        throw new InvalidAuthException("토큰이 유효하지 않습니다.");
    }
}
