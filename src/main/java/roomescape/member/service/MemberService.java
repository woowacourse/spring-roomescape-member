package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findMemberList() {
        Optional<List<Member>> membersOptional = memberRepository.findAll();

        return membersOptional.orElseThrow(() -> new IllegalArgumentException("멤버가 없습니다."));
    }
}
