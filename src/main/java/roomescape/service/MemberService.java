package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.MemberLoginRequest;
import roomescape.domain.Member;
import roomescape.domain.exception.InvalidRequestException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void login(final MemberLoginRequest loginRequest) {
        final Member member = memberRepository.fetchByEmail(loginRequest.email());
        if (hasInvalidPassword(loginRequest, member)) {
            throw new InvalidRequestException("Invalid password"); // TODO 예외 수정
        }
    }

    private boolean hasInvalidPassword(final MemberLoginRequest loginRequest, final Member member) {
        return !member.hasValidPassword(loginRequest.password());
    }
}
