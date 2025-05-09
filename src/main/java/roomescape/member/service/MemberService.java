package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean isExistsByEmail(final String email) {
        return memberRepository.isExistsByEmail(email);
    }

    public Member findById(final Long id) {
        return memberRepository.findById(id);
    }
}
