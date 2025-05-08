package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmailAndPassword(final String email, final String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Member findById(final Long memberId) {
        return memberRepository.findById(memberId);
    }
}
