package roomescape.member.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.NotFoundException;
import roomescape.member.application.dto.MemberDto;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return MemberDto.from(members);
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("memberId", id));
        return MemberDto.from(member);
    }

    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }
}
