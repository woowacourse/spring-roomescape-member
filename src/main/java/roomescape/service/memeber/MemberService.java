package roomescape.service.memeber;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberResponse;
import roomescape.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> readAll() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
