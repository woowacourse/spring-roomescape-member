package roomescape.core.service;

import org.springframework.stereotype.Service;
import roomescape.core.config.TokenProvider;
import roomescape.core.domain.Member;
import roomescape.core.dto.auth.TokenRequest;
import roomescape.core.dto.auth.TokenResponse;
import roomescape.core.dto.member.MemberResponse;
import roomescape.core.repository.MemberRepository;

@Service
public class MemberService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(final TokenProvider tokenProvider, final MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(final TokenRequest request) {
        final Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (member == null) {
            throw new IllegalArgumentException("올바르지 않은 이메일 또는 비밀번호입니다.");
        }
        return new TokenResponse(tokenProvider.createToken(member.getEmail()));
    }

    public MemberResponse findMemberByToken(final String token) {
        final String email = tokenProvider.getPayload(token);
        final Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return new MemberResponse(member.getName());
    }
}
