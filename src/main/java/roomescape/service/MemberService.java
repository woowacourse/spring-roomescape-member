package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.MemberRequest;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Name getNameByEmail(String email) {
        return memberRepository.findNameByEmail(email);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Name getUserNameByUserId(Long userId) {
        return memberRepository.findNameById(userId);
    }

    public List<Member> getAllUsers() {
        return memberRepository.findAllUsers();
    }

    public Member addMember(MemberRequest memberRequest) {
        return memberRepository.addMember(memberRequest);
    }

    public Member findByEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail);
    }
}
