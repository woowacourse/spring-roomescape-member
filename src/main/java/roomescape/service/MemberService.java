package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.MemberLoginRequest;
import roomescape.controller.MemberResponse;
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
        final String accessToken = jwtTokenProvider.generateToken(request.email());
        return new TokenResponse(accessToken);
    }

    public Member login(final MemberLoginRequest loginRequest) {
        final Member member = memberRepository.fetchByEmail(loginRequest.email());
        if (hasInvalidPassword(loginRequest, member)) {
            throw new InvalidRequestException("Invalid password"); // TODO 예외 수정
        }
        return member;
    }

    public MemberResponse findMemberByToken(final String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        final String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public MemberResponse findMember(final String principal) {
        final Member member = memberRepository.fetchByEmail(principal);

        return new MemberResponse(member.getName());
    }

    private boolean hasInvalidPassword(final MemberLoginRequest loginRequest, final Member member) {
        return !member.hasValidPassword(loginRequest.password());
    }
}
