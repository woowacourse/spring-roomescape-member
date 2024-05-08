package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.controller.member.dto.MemberResponse;
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

    public MemberResponse findMemberByToken(final String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        final String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public MemberResponse findMember(final String principal) {
        final Member member = memberRepository.fetchById(Long.parseLong(principal));

        return new MemberResponse(member.getName());
    }
}
