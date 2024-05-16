package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.member.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalUserRequestException("존재하지 않는 사용자 입니다."));
    }
}
