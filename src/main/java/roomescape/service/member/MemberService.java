package roomescape.service.member;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.repository.member.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
