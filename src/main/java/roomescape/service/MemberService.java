package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.dto.TokenResponse;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.domain.Member;
import roomescape.domain.exception.InvalidRequestException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;
import roomescape.service.exception.InvalidTokenException;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean checkInvalidLogin(final String email, final String password) {
        final Member member = memberRepository.fetchByEmail(email);
        return !member.isCorrectPassword(password);
    }

    public TokenResponse createToken(final MemberLoginRequest request) {
        if (checkInvalidLogin(request.email(), request.password())) {
            throw new InvalidRequestException("Invalid email or password");
        }
        final Member member = memberRepository.fetchByEmail(request.email());

        final String accessToken = jwtTokenProvider.generateToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(final String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("유효하지 않는 토큰입니다.");
        }
        final String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    private Member findMember(final String principal) {
        return memberRepository.fetchById(Long.parseLong(principal));
    }
}
