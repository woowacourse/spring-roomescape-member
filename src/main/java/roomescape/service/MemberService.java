package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.global.exception.RoomescapeException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String findNameFromId(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RoomescapeException("사용자를 찾을 수 없습니다."));
        return member.getName();
    }
}
