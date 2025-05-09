package roomescape.auth.service;

import org.springframework.stereotype.Component;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.exception.AuthException;
import roomescape.auth.infrastructure.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtProvider jwtProvider, final MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        Member member = getByMember(loginRequest);

        String accessToken = jwtProvider.createToken(member);
        return new LoginResponse(accessToken);
    }

    private Member getByMember(final LoginRequest loginRequest) {
        return memberRepository.findByMember(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new AuthException("존재하지 않은 email 또는 비밀번호입니다."));
    }
}
