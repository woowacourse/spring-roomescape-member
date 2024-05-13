package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findMemberList() {
        List<Member> members = memberRepository.findAll();

        if (members.isEmpty()) {
            throw new IllegalArgumentException("멤버가 없습니다.");
        }

        return members;
    }
}
