package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.dto.SignupRequest;
import roomescape.member.exception.MemberDuplicatedException;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member signup(final SignupRequest signupRequest) {
        Member member = Member.withUnassignedId(signupRequest.name(), signupRequest.email(), signupRequest.password(),
                MemberRole.USER);
        if (memberRepository.existsByEmailAndPassword(signupRequest.email(), signupRequest.password())) {
            throw new MemberDuplicatedException("이미 존재하는 회원입니다.");
        }
        return memberRepository.save(member);
    }
}
