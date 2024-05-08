package roomescape.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.MemberNotExistException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotExistException("해당 이메일의 사용자가 없습니다."));
    }
}
