package roomescape.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.domain.Member;
import roomescape.domain.exception.InvalidRequestException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

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
        return !member.hasValidPassword(password);
    }

    public TokenResponse createToken(final MemberLoginRequest request) {
        if (checkInvalidLogin(request.email(), request.password())) {
            throw new InvalidRequestException("Invalid email or password");
        }
        final Member member = memberRepository.fetchByEmail(request.email());

        final String accessToken = jwtTokenProvider.generateToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(final String token) { //TODO domain 반환해야 재사용 가능!
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            final String payload = jwtTokenProvider.getPayload(token);
            return findMember(payload);
        } catch (final ExpiredJwtException e) {
            return null;
        }
    }

    private Member findMember(final String principal) {
        return memberRepository.fetchById(Long.parseLong(principal));
    }
}
