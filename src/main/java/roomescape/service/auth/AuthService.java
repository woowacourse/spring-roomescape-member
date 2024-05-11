package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.controller.login.TokenRequest;
import roomescape.controller.login.TokenResponse;
import roomescape.controller.member.MemberResponse;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.MemberRepository;
import roomescape.service.auth.exception.MemberNotFoundException;
import roomescape.service.auth.exception.TokenNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재 하지 않는 로그인 정보 입니다."));
    }

    public Member getMemberByToken(String token) {
        validateTokenBlank(token);
        String payload = jwtTokenProvider.getPayload(token);

        return getMemberByEmail(payload);
    }

    private void validateTokenBlank(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenNotFoundException("토큰이 존재 하지 않습니다.");
        }
    }

    public TokenResponse createToken(TokenRequest request) {
        validateInformation(request);
        String accessToken = jwtTokenProvider.createToken(request.email());

        return new TokenResponse(accessToken);
    }

    private void validateInformation(TokenRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.email());
        if (member.isEmpty() || !member.get().getPassword().equals(request.password())) {
            throw new UnauthorizedException("로그인 정보가 일치하지 않습니다.");
        }
    }
}
