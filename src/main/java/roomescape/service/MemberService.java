package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.repository.MemberRepository;
import roomescape.domain.Member;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAllMembers() {
        return memberRepository.allMembers();
    }
}
