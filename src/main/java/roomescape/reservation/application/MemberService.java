package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.NotFoundException;
import roomescape.reservation.application.dto.info.MemberDto;
import roomescape.reservation.domain.Member;
import roomescape.reservation.domain.repository.MemberRepository;

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
}
