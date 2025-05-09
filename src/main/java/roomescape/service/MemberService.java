package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.repository.member.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllByRole(MemberRole role) {
        return memberRepository.findAllByRole(role);
    }
}
