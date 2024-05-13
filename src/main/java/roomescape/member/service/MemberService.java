package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findMemberList() {
        List<Member> members = memberRepository.findAll();
        validateMemberExists(members);

        return members;
    }

    private void validateMemberExists(List<Member> members) {
        if (members.isEmpty()) {
            throw new NoSuchElementException("멤버가 없습니다.");
        }
    }
}
