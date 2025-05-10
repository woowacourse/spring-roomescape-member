package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.MemberProfileResponse;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberProfileResponse findMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return MemberProfileResponse.from(member);
    }
}
