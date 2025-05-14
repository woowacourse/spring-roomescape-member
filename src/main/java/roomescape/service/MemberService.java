package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.SignUpRequest;
import roomescape.dto.SignUpResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SignUpResponse signUpMember(final SignUpRequest request) {
        Member member = new Member(request.name(), request.email(), request.password(), "USER");
        Member saved = memberRepository.save(member);
        return SignUpResponse.of(saved);
    }

    public Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new DataNotFoundException(String.format("[ERROR] %d번에 해당하는 멤버가 없습니다.", memberId)));
    }
}
