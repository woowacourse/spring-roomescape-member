package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Member;
import roomescape.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
