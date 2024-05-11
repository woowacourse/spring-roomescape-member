package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequestDto;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public String createToken(final LoginRequestDto request) {
        final Member member = memberRepository.findByEmail(request.getEmail());

        if (member.hasMatchedPassword(request.getPassword())) {
            return jwtTokenProvider.createToken(request.getEmail());
        }
        throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
    }

    @Transactional(readOnly = true)
    public Member findMemberByToken(final String token) {
        if (token == null || jwtTokenProvider.isExpiredToken(token)) {
            throw new AuthorizationException("토큰이 없거나 만료되었습니다.");
        }
        final String email = jwtTokenProvider.getPayload(token);
        return memberRepository.findByEmail(email);
    }
}
