package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.result.MemberResult;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResult> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResult::from)
                .toList();
    }
}
